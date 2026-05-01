package com.futurelin.r2aot.compact.jei;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.common.recipe.EnchantingChargeRecipe;
import com.futurelin.r2aot.common.recipe.RuneBlackRecipe;
import com.futurelin.r2aot.compact.jei.category.EnchantingChargeRecipeCategory;
import com.futurelin.r2aot.compact.jei.category.RuneBlackCategory;
import com.futurelin.r2aot.registry.ItemsRegistry;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import owmii.powah.block.Blcks;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(R2AoTCore.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new RuneBlackCategory(registration.getJeiHelpers().getGuiHelper()),
                new EnchantingChargeRecipeCategory(registration.getJeiHelpers().getGuiHelper())
                );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<RuneBlackRecipe> runeBlackRecipeList = new ArrayList<>();
        List<EnchantingChargeRecipe> enchantingChargeRecipeList = new ArrayList<>();

        for (Recipe<?> i : recipeManager.getRecipes()) {
            if (i instanceof RuneBlackRecipe runeBlackRecipe) {
                runeBlackRecipeList.add(runeBlackRecipe);
            }
            if (i instanceof EnchantingChargeRecipe enchantingChargeRecipe) {
                enchantingChargeRecipeList.add(enchantingChargeRecipe);
            }
        }

        registration.addRecipes(JEIRecipeType.RUNE_BLACK_RECIPE_TYPE, runeBlackRecipeList);
        registration.addRecipes(JEIRecipeType.ENCHANTING_CHARGE_RECIPE_TYPE, enchantingChargeRecipeList);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ItemsRegistry.CHALK_BLACK.get()), JEIRecipeType.RUNE_BLACK_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ENCHANTING_APP_BLOCK), JEIRecipeType.ENCHANTING_CHARGE_RECIPE_TYPE);
        Blcks.THERMO_GENERATOR.getAll().forEach(block -> registration.addRecipeCatalyst(new ItemStack(block), JEIRecipeType.ENCHANTING_CHARGE_RECIPE_TYPE));
    }
}
