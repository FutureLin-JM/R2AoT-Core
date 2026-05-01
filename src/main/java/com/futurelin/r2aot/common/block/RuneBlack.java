package com.futurelin.r2aot.common.block;

import com.futurelin.r2aot.common.block.tile.RuneBlackTile;
import com.futurelin.r2aot.registry.BlockRegistry;
import com.futurelin.r2aot.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class RuneBlack extends BaseEntityBlock {
    public static VoxelShape shape = Block.box(0.0d, 0.0d, 0.0d, 16d, 0.5d, 16d);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty ENERGY_LEVEL = IntegerProperty.create("energy", 0, 4);

    public RuneBlack() {
        super(Properties.of().noCollission().noOcclusion().strength(0.0f, 0.0f));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(ENERGY_LEVEL, 0));
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        super.entityInside(pState, pLevel, pPos, pEntity);
        if (!pLevel.isClientSide()) {
            AABB boundingBox = shape.bounds().move(pPos);
            List<ItemEntity> itemEntities = pLevel.getEntitiesOfClass(ItemEntity.class, boundingBox);

            if (!itemEntities.isEmpty()) {
                BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
                if (!(blockEntity instanceof RuneBlackTile runeTile)) {
                    return;
                }

                for (ItemEntity itemEntity : itemEntities) {
                    if (itemEntity.isCurrentlyGlowing()) {
                        continue;
                    }

                    runeTile.onSteppedByItem(pLevel, itemEntity);
                }
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(ItemsRegistry.CHALK_BLACK.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, ENERGY_LEVEL);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public static int getEnergy(BlockState state) {
        return state.getValue(ENERGY_LEVEL);
    }

    public static BlockState setEnergy(BlockState state, int energy) {
        return state.setValue(ENERGY_LEVEL, Math.min(energy, 4));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RuneBlackTile(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, BlockRegistry.RUNE_BLACK_TILE.get(),
                (level, pos, state, blockEntity) -> blockEntity.tick(level, pos, state));
    }
}
