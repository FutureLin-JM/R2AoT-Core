package com.futurelin.r2aot.common.item;

import com.futurelin.r2aot.api.item.AbstractBaseItemWithTooltip;
import com.futurelin.r2aot.api.item.KeyType;
import com.futurelin.r2aot.registry.BlockRegistry;
import com.klikli_dev.occultism.registry.OccultismSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChalkBlack extends AbstractBaseItemWithTooltip {
    public ChalkBlack(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        Player player = pContext.getPlayer();

        if (level.isClientSide()) {
            return super.useOn(pContext);
        }

        Direction clickedFace = pContext.getClickedFace();
        BlockPos targetPos = pos.relative(clickedFace);
        boolean isReplaceable = level.getBlockState(targetPos).canBeReplaced();
        boolean isRune = level.getBlockState(pos).is(BlockRegistry.RUNE_BLACK.get());

        if (clickedFace == Direction.UP && isReplaceable && !isRune) {
            BlockState placement = BlockRegistry.RUNE_BLACK.get().getStateForPlacement(new BlockPlaceContext(pContext));
            level.setBlockAndUpdate(targetPos, placement);

            level.playSound(null, pos, OccultismSounds.CHALK.get(), SoundSource.PLAYERS, 0.5f,
                        1 + 0.5f * player.getRandom().nextFloat());
            pContext.getItemInHand().hurtAndBreak(1, player, (t) -> {});
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        addTooltipWhileKeyDown(KeyType.SHIFT, tooltips, stack, 2);
    }
}
