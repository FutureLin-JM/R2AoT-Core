package com.futurelin.r2aot.compact.kubejs.recipes;

import com.futurelin.r2aot.compact.kubejs.components.ArsComponents;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface EnchantingChargeRecipeJS {

    RecipeKey<InputItem[]> PEDESTAL_ITEMS = ArsComponents.INPUT_ITEM.asArray().key("pedestalItems");

    RecipeKey<InputItem[]> REAGENT = ItemComponents.INPUT.asArray().key("reagent");

    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("output");

    RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energyCost");

    RecipeKey<Integer> SOURCE = NumberComponent.INT.key("sourceCost").alt("source").optional(0);

    RecipeKey<Boolean> KEEP_NBT = BooleanComponent.BOOLEAN.key("keepNbtOfReagent").alt("keepNbt").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema(PEDESTAL_ITEMS, REAGENT, OUTPUT, ENERGY, SOURCE, KEEP_NBT);
}
