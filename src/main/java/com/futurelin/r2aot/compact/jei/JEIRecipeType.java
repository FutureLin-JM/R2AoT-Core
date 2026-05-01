package com.futurelin.r2aot.compact.jei;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.common.recipe.EnchantingChargeRecipe;
import com.futurelin.r2aot.common.recipe.RuneBlackRecipe;
import com.futurelin.r2aot.registry.RecipeRegistry;
import mezz.jei.api.recipe.RecipeType;

public class JEIRecipeType {
    public static final String nameSpace = R2AoTCore.MOD_ID;
    public static final RecipeType<RuneBlackRecipe> RUNE_BLACK_RECIPE_TYPE = RecipeType.create(nameSpace, RecipeRegistry.RUNE_BLACK_RECIPE_ID, RuneBlackRecipe.class);
    public static final RecipeType<EnchantingChargeRecipe> ENCHANTING_CHARGE_RECIPE_TYPE = RecipeType.create(nameSpace, RecipeRegistry.ENCHANTING_CHARGE_RECIPE_ID, EnchantingChargeRecipe.class);
}
