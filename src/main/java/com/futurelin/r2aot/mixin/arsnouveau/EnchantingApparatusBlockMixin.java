package com.futurelin.r2aot.mixin.arsnouveau;

import com.futurelin.r2aot.TranslationKeys;
import com.futurelin.r2aot.registry.RecipeRegistry;
import com.hollingsworth.arsnouveau.common.block.ArcaneCore;
import com.hollingsworth.arsnouveau.common.block.EnchantingApparatusBlock;
import com.hollingsworth.arsnouveau.common.block.TickableModBlock;
import com.hollingsworth.arsnouveau.common.block.tile.EnchantingApparatusTile;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import owmii.powah.block.thermo.ThermoBlock;

import java.util.List;

@Mixin(EnchantingApparatusBlock.class)
public abstract class EnchantingApparatusBlockMixin extends TickableModBlock {

    @Redirect(
            method = "use",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lcom/hollingsworth/arsnouveau/common/util/PortUtil;sendMessage(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;)V",
                            remap = false
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
            )
    )
    private Block allowNewBlockType(BlockState state) {
        Block originalBlock = state.getBlock();
        if (originalBlock instanceof ArcaneCore) {
            return originalBlock;
        }
        if (originalBlock instanceof ThermoBlock) {
            return BlockRegistry.ARCANE_CORE_BLOCK.get();
        }
        return originalBlock;
    }

    @Redirect(
            method = "use",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lcom/hollingsworth/arsnouveau/common/network/Networking;sendToNearby(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/lang/Object;)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lcom/hollingsworth/arsnouveau/api/enchanting_apparatus/IEnchantingRecipe;consumesSource()Z"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/hollingsworth/arsnouveau/common/util/PortUtil;sendMessage(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;)V"
            ),
            remap = false
    )
    private void sendNoRecipeMessage(Entity playerEntity, Component component, BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (level.getBlockState(pos.below()).getBlock() instanceof ThermoBlock) {
            PortUtil.sendMessage(playerEntity, component);
            return;
        }
        if (!(level.getBlockEntity(pos) instanceof EnchantingApparatusTile tile)) {
            PortUtil.sendMessage(playerEntity, component);
            return;
        }

        ItemStack reagent = player.getItemInHand(handIn);
        List<ItemStack> pedestalItems = tile.getPedestalItems();
        boolean expectEnergyRecipe = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.ENCHANTING_CHARGE_RECIPE.get())
                .stream()
                .anyMatch(recipe -> recipe.isMatchIgnoringThermo(pedestalItems, reagent, tile, player));

        if (expectEnergyRecipe) {
            PortUtil.sendMessage(playerEntity, Component.translatable(TranslationKeys.MESSAGE_APPARATUS_NO_THERMO));
            return;
        }
        PortUtil.sendMessage(playerEntity, component);
    }
}
