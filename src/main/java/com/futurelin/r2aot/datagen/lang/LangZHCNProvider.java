package com.futurelin.r2aot.datagen.lang;

import com.futurelin.r2aot.TranslationKeys;
import com.futurelin.r2aot.api.datagen.LangProvider;
import com.futurelin.r2aot.registry.BlockRegistry;
import com.futurelin.r2aot.registry.EntitiesRegistry;
import com.futurelin.r2aot.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;

public class LangZHCNProvider extends LangProvider {
    public LangZHCNProvider(PackOutput output) {
        super(output, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(TranslationKeys.ITEM_GROUP, "R2AoT");

        add(TranslationKeys.TOOLTIP_HOLD, "按住 [%s] 查看更多信息");
        add(TranslationKeys.TOOLTIP_COIN_VALUE, "价值: %s，右键存入账户");
        add(TranslationKeys.TOOLTIP_COIN_WITHDRAW, "价值: %s，左键取出 1 个，右键取出 1 组");

        add(TranslationKeys.MESSAGE_COIN_DEPOSIT, "已存入 %s");
        add(TranslationKeys.MESSAGE_NOT_WITHDRAW, "余额不足！");
        add(TranslationKeys.MESSAGE_APPARATUS_NO_ENERGY, "无法抽取足够的能量。");
        add(TranslationKeys.MESSAGE_APPARATUS_NO_THERMO, "该配方下方必须放置任意热力发电机作为核心。");

        add(TranslationKeys.JEI_ENCHANTING_CHARGE, "充能附魔");
        add(TranslationKeys.JEI_ENCHANTING_CHARGE_ENERGY, "能量消耗：%sFE");

        addItem(ItemsRegistry.CHALK_BLACK, "暗黑粉笔");
        addItem(ItemsRegistry.SILVER_COIN, "银币");
        addItem(ItemsRegistry.GOLD_COIN, "金币");
        addItem(ItemsRegistry.Crystal_COIN, "水晶币");

        addBlock(BlockRegistry.RUNE_BLACK, "暗黑符文");

        addEntityType(EntitiesRegistry.RITUALMASTER, "仪式大师");

        addTooltip(BlockRegistry.RUNE_BLACK.get(), 1, "暗黑符文每秒充能1-5点能量");
        addTooltip(BlockRegistry.RUNE_BLACK.get(), 2, "转化暗黑莲花消耗25点能量");
        addTooltip(BlockRegistry.RUNE_BLACK.get(), 3, "转化时可以调用周围8个暗黑符文的能量");

        addTooltip(ItemsRegistry.CHALK_BLACK.get(), 1, "右键画出暗黑符文");
        addTooltip(ItemsRegistry.CHALK_BLACK.get(), 2, "暗黑符文可用于转化暗黑莲花");

        add("job.r2aot.ritualmaster", "仪式大师");
    }
}
