package com.futurelin.r2aot.datagen.lang;

import com.futurelin.r2aot.TranslationKeys;
import com.futurelin.r2aot.api.datagen.LangProvider;
import com.futurelin.r2aot.registry.BlockRegistry;
import com.futurelin.r2aot.registry.EntitiesRegistry;
import com.futurelin.r2aot.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;

public class LangENUSProvider extends LangProvider {
    public LangENUSProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(TranslationKeys.ITEM_GROUP, "R2AoT");

        add(TranslationKeys.TOOLTIP_HOLD, "Hold [%s] for more info");
        add(TranslationKeys.TOOLTIP_COIN_VALUE, "Value: %s, Right-click to deposit");
        add(TranslationKeys.TOOLTIP_COIN_WITHDRAW, "Value: %s, Left-click to withdraw 1, Right-click for a stack");

        add(TranslationKeys.MESSAGE_COIN_DEPOSIT, "Deposited %s");
        add(TranslationKeys.MESSAGE_NOT_WITHDRAW, "Insufficient balance!");
        add(TranslationKeys.MESSAGE_APPARATUS_NO_ENERGY, "Cannot draw sufficient energy.");
        add(TranslationKeys.MESSAGE_APPARATUS_NO_THERMO, "Any Thermal Generator must be placed below this recipe as the core.");

        add(TranslationKeys.JEI_ENCHANTING_CHARGE, "Enchanting Charge");
        add(TranslationKeys.JEI_ENCHANTING_CHARGE_ENERGY, "Energy: %sFE");

        add(ItemsRegistry.CHALK_BLACK.get(), "Black Chalk");
        add(ItemsRegistry.SILVER_COIN.get(), "Silver Coin");
        add(ItemsRegistry.GOLD_COIN.get(), "Gold Coin");
        add(ItemsRegistry.Crystal_COIN.get(), "Crystal Coin");

        add(BlockRegistry.RUNE_BLACK.get(), "Black Rune");

        addEntityType(EntitiesRegistry.RITUALMASTER, "Ritualmaster");

        addTooltip(BlockRegistry.RUNE_BLACK.get(), 1, "Black Rune charges 1-5 energy per second");
        addTooltip(BlockRegistry.RUNE_BLACK.get(), 2, "Converting Blacker Lotus consumes 25 energy");
        addTooltip(BlockRegistry.RUNE_BLACK.get(), 3, "Can draw energy from the 8 surrounding Black Rune");

        addTooltip(ItemsRegistry.CHALK_BLACK.get(), 1, "Right-click to draw a Black Rune");
        addTooltip(ItemsRegistry.CHALK_BLACK.get(), 2, "Black Rune can be used to convert Blacker Lotus");

        add("job.r2aot.ritualmaster", "Ritualmaster");
    }
}
