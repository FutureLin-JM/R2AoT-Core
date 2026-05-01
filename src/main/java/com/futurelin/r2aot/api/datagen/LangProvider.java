package com.futurelin.r2aot.api.datagen;

import com.futurelin.r2aot.R2AoTCore;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class LangProvider extends LanguageProvider {
    public LangProvider(PackOutput output, String locale) {
        super(output, R2AoTCore.MOD_ID, locale);
    }

    public void addTooltip(ItemLike key, int index, String value) {
        String rawKey = key.asItem().getDescriptionId();
        String langKey = "tooltip" + rawKey.substring(rawKey.indexOf(".")) + "_" + index;
        add(langKey, value);
    }

    public void addTooltip(ItemLike key, String value) {
        String rawKey = key.asItem().getDescriptionId();
        String langKey = "tooltip" + rawKey.substring(rawKey.indexOf("."));
        add(langKey, value);
    }
}
