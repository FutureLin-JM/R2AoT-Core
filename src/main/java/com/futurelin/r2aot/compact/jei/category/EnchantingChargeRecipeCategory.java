package com.futurelin.r2aot.compact.jei.category;

import com.futurelin.r2aot.TranslationKeys;
import com.futurelin.r2aot.common.recipe.EnchantingChargeRecipe;
import com.futurelin.r2aot.compact.jei.JEIRecipeType;
import com.hollingsworth.arsnouveau.client.jei.EnchantingApparatusRecipeCategory;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class EnchantingChargeRecipeCategory extends EnchantingApparatusRecipeCategory<EnchantingChargeRecipe> {
    public EnchantingChargeRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public Component getTitle() {
        return Component.translatable(TranslationKeys.JEI_ENCHANTING_CHARGE);
    }

    @Override
    public RecipeType<EnchantingChargeRecipe> getRecipeType() {
        return JEIRecipeType.ENCHANTING_CHARGE_RECIPE_TYPE;
    }

    @Override
    public void draw(EnchantingChargeRecipe recipe, @NotNull IRecipeSlotsView slotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font renderer = Minecraft.getInstance().font;
        guiGraphics.drawString(renderer, Component.translatable(TranslationKeys.JEI_ENCHANTING_CHARGE_ENERGY, recipe.energyCost), 0, 0, 10,false);

        if (recipe.consumesSource()) {
            guiGraphics.drawString(renderer, Component.translatable("ars_nouveau.source", recipe.sourceCost), 0, 100, 10, false);
        }
    }
}
