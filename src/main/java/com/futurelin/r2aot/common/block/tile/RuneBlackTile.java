package com.futurelin.r2aot.common.block.tile;

import com.futurelin.r2aot.common.block.RuneBlack;
import com.futurelin.r2aot.common.recipe.RuneBlackRecipe;
import com.futurelin.r2aot.registry.BlockRegistry;
import com.futurelin.r2aot.registry.RecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


public class RuneBlackTile extends BlockEntity {

    private int energy = 0;
    private static final int MAX_ENERGY = 100;
    private static final int ENERGY_PER_LEVEL = 25;

    public RuneBlackTile(BlockPos pPos, BlockState pBlockState) {
        super(BlockRegistry.RUNE_BLACK_TILE.get(), pPos, pBlockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) {
            return;
        }

        if (level.getGameTime() % 20 == 0 && energy < MAX_ENERGY) {
            randomEnergy(level);
            updateEnergyLevel(state, level, pos);
            setChanged();
        }
    }

    private void randomEnergy(Level level) {
        int randomEnergy = level.getRandom().nextInt(5) + 1;
        energy = Math.min(energy + randomEnergy, MAX_ENERGY);
    }

    public void onSteppedByItem(Level level, ItemEntity itemEntity) {
        if (level == null || level.isClientSide) {
            return;
        }

        ItemStack itemStack = itemEntity.getItem();
        if (itemStack.isEmpty()) {
            return;
        }

        for (RuneBlackRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeRegistry.RUNE_BLACK_TYPE.get())) {
            if (recipe.matches(itemStack, level)) {
                int energyCostPerItem = recipe.getEnergyCost();
                
                if (energyCostPerItem <= 0) {
                    return;
                }

                int selfEnergy = energy;
                int neighborsTotal = getNeighborsTotalEnergy(level, worldPosition);
                int totalEnergy = selfEnergy + neighborsTotal;
                
                int itemCount = itemStack.getCount();
                int maxPossibleCount = totalEnergy / energyCostPerItem;
                
                if (maxPossibleCount <= 0) {
                    return;
                }
                
                int convertCount = Math.min(itemCount, maxPossibleCount);
                int removeEnergy = convertCount * energyCostPerItem;
                
                performRecipe(level, recipe, convertCount);
                
                if (convertCount == itemCount) {
                    itemEntity.discard();
                } else {
                    ItemStack remainingStack = itemStack.copy();
                    remainingStack.setCount(itemCount - convertCount);
                    itemEntity.setItem(remainingStack);
                }
                
                deductEnergy(removeEnergy, selfEnergy, level, worldPosition);
                return;
            }
        }
    }

    private int getNeighborsTotalEnergy(Level level, BlockPos centerPos) {
        int total = 0;
        
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                
                BlockPos neighborPos = centerPos.offset(x, 0, z);
                BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
                
                if (neighborEntity instanceof RuneBlackTile neighborTile) {
                    total += neighborTile.getEnergy();
                }
            }
        }
        
        return total;
    }

    private void deductEnergy(int totalToDeduct, int selfEnergy, Level level, BlockPos centerPos) {
        int remaining = totalToDeduct;
        
        int takeFromSelf = Math.min(selfEnergy, remaining);
        energy = selfEnergy - takeFromSelf;
        remaining -= takeFromSelf;
        
        if (remaining > 0) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && z == 0) continue;
                    if (remaining <= 0) break;
                    
                    BlockPos neighborPos = centerPos.offset(x, 0, z);
                    BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
                    
                    if (neighborEntity instanceof RuneBlackTile neighborTile) {
                        int neighborEnergy = neighborTile.getEnergy();
                        if (neighborEnergy > 0) {
                            int take = Math.min(neighborEnergy, remaining);
                            neighborTile.energy = neighborEnergy - take;
                            neighborTile.updateEnergyLevel(
                                level.getBlockState(neighborPos),
                                level,
                                neighborPos
                            );
                            neighborTile.setChanged();
                            remaining -= take;
                        }
                    }
                }
            }
        }
        
        updateEnergyLevel(level.getBlockState(centerPos), level, centerPos);
        setChanged();
    }

    private void performRecipe(Level level, RuneBlackRecipe recipe, int count) {
        ItemStack output = recipe.assemble(null, level.registryAccess());
        output.setCount(count);
        
        ItemEntity resultEntity = new ItemEntity(EntityType.ITEM, level);
        resultEntity.setItem(output);
        resultEntity.setPos(
            worldPosition.getX() + 0.5,
            worldPosition.getY() + 0.05,
            worldPosition.getZ() + 0.5
        );
        resultEntity.setDeltaMovement(new Vec3(0, 0.3, 0));
        resultEntity.setGlowingTag(true);
        level.addFreshEntity(resultEntity);
    }

    private void updateEnergyLevel(BlockState state, Level level, BlockPos pos) {
        if (state.hasProperty(RuneBlack.ENERGY_LEVEL)) {
            int newEnergyLevel = (energy == MAX_ENERGY) ? 4 : (energy / ENERGY_PER_LEVEL);
            int currentEnergyLevel = state.getValue(RuneBlack.ENERGY_LEVEL);

            if (newEnergyLevel != currentEnergyLevel) {
                level.setBlock(pos, state.setValue(RuneBlack.ENERGY_LEVEL, newEnergyLevel), 3);
            }
        }
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("energy", energy);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("energy")) {
            energy = pTag.getInt("energy");
        }
    }
}
