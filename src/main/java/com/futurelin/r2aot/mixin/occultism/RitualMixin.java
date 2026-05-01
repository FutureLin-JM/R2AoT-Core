package com.futurelin.r2aot.mixin.occultism;

import com.klikli_dev.occultism.common.blockentity.GoldenSacrificialBowlBlockEntity;
import com.klikli_dev.occultism.common.ritual.Ritual;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.futurelin.r2aot.common.entity.RitualmasterEntity.DROPPED_RESULT;

@Mixin(Ritual.class)
public abstract class RitualMixin {

    @Inject(method = "dropResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void addTagForResult(Level level, BlockPos goldenBowlPosition, GoldenSacrificialBowlBlockEntity blockEntity, Player castingPlayer, ItemStack stack, CallbackInfo ci, double angle, ItemEntity entity) {
        entity.addTag(DROPPED_RESULT);
    }
}
