package com.futurelin.r2aot.api.mixin.accessor;

import com.klikli_dev.occultism.common.entity.spirit.SpiritEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

public interface IRitualRecipeAccessor {

    boolean matches(ItemStackHandler inv, Level level, SpiritEntity entity);
}
