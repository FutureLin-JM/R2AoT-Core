package com.futurelin.r2aot.compact.jei.category;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.common.recipe.RuneBlackRecipe;
import com.futurelin.r2aot.compact.jei.JEIRecipeType;
import com.futurelin.r2aot.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RuneBlackCategory implements IRecipeCategory<RuneBlackRecipe> {
    public IDrawable background;
    public IDrawable icon;
    public IDrawable slotDrawable;
    public IDrawable arrow;
    public static final ResourceLocation runeBlackTexture = ResourceLocation.fromNamespaceAndPath(R2AoTCore.MOD_ID, "textures/block/rune_black_full.png");

    public RuneBlackCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(150, 60);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.RUNE_BLACK.get()));
        slotDrawable = guiHelper.getSlotDrawable();
        arrow = guiHelper.createAnimatedRecipeArrow(100);
    }

    @Override
    public RecipeType<RuneBlackRecipe> getRecipeType() {
        return JEIRecipeType.RUNE_BLACK_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.r2aot.rune_black");
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(RuneBlackRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        slotDrawable.draw(guiGraphics, 9, 21);
        slotDrawable.draw(guiGraphics, 115, 21);

        guiGraphics.blit(runeBlackTexture, 46, 22, 0, 0, 16, 16, 16, 16);
        arrow.draw(guiGraphics, 80, 22);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RuneBlackRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 22).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 22).addItemStack(recipe.getResultItem(null));
    }
}
