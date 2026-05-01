package com.futurelin.r2aot.mixin.arsnouveau;

import com.futurelin.r2aot.TranslationKeys;
import com.futurelin.r2aot.common.recipe.EnchantingChargeRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.IEnchantingRecipe;
import com.hollingsworth.arsnouveau.client.particle.ColorPos;
import com.hollingsworth.arsnouveau.common.block.tile.EnchantingApparatusTile;
import com.hollingsworth.arsnouveau.common.block.tile.SingleItemTile;
import com.hollingsworth.arsnouveau.common.network.HighlightAreaPacket;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.powah.block.thermo.ThermoTile;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantingApparatusTile.class)
public abstract class EnchantingApparatusTileMixin extends SingleItemTile {

    public EnchantingApparatusTileMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow(remap = false)
    public abstract IEnchantingRecipe getRecipe(ItemStack stack, @Nullable Player playerEntity);

    @Inject(method = "craftingPossible", at = @At("TAIL"), cancellable = true, remap = false)
    public void craftingPossibleOnThermo(ItemStack stack, Player playerEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }
        IEnchantingRecipe recipe = this.getRecipe(stack, playerEntity);
        BlockEntity be = this.level.getBlockEntity(this.worldPosition.below());
        if (recipe instanceof EnchantingChargeRecipe enchantingChargeRecipe) {
            if (be instanceof ThermoTile thermoTile) {
                int energyCost = enchantingChargeRecipe.getEnergyCost();
                long currentEnergy =  thermoTile.getEnergy().getStored();
                if (energyCost > currentEnergy) {
                    List<ColorPos> colorPos = new ArrayList<>();
                    colorPos.add(ColorPos.centeredAbove(thermoTile.getBlockPos()));
                    Networking.sendToNearby(level, worldPosition, new HighlightAreaPacket(colorPos, 60));
                    PortUtil.sendMessage(playerEntity, Component.translatable(TranslationKeys.MESSAGE_APPARATUS_NO_ENERGY));
                    cir.setReturnValue(false);
                }
            } else {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "attemptCraft", at = @At(
            value = "INVOKE",
            target = "Lcom/hollingsworth/arsnouveau/api/enchanting_apparatus/IEnchantingRecipe;consumesSource()Z"),
            remap = false
    )
    public void attemptCraftThermo(ItemStack catalyst, Player playerEntity, CallbackInfoReturnable<Boolean> cir) {
        IEnchantingRecipe recipe = this.getRecipe(catalyst, playerEntity);
        if (recipe instanceof EnchantingChargeRecipe enchantingChargeRecipe) {
            ThermoTile thermoTile = (ThermoTile) level.getBlockEntity(this.worldPosition.below());
            int energyCost = enchantingChargeRecipe.getEnergyCost();
            long maxExtractEnergy = thermoTile.getEnergy().getMaxExtract();
            if (maxExtractEnergy <= 0) {
                return;
            }

            long remainingEnergy = energyCost;
            while (remainingEnergy > 0) {
                long batchEnergy = Math.min(remainingEnergy, maxExtractEnergy);
                if (batchEnergy <= 0) {
                    break;
                }

                thermoTile.extractEnergy(batchEnergy, false, Direction.UP);
                remainingEnergy -= batchEnergy;
            }
        }
    }

}
