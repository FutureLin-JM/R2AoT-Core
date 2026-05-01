package com.futurelin.r2aot.common.recipe;

import com.futurelin.r2aot.registry.RecipeRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneBlackRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;
    private final int energyCost;

    public RuneBlackRecipe(ResourceLocation id, Ingredient input, ItemStack output, int energyCost) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.energyCost = energyCost;
    }

    @Override
    public boolean matches(Container inventory, Level level) {
        return input.test(inventory.getItem(0));
    }

    public boolean matches(ItemStack itemStack, Level level) {
        return input.test(itemStack);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public Ingredient getInput() {
        return input;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.RUNE_BLACK_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.RUNE_BLACK_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<RuneBlackRecipe> {
        @Override
        public RuneBlackRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));

            JsonObject outputJson = GsonHelper.getAsJsonObject(json, "output");
            String outputId = GsonHelper.getAsString(outputJson, "item");
            int outputCount = GsonHelper.getAsInt(outputJson, "count", 1);
            ItemStack output = new ItemStack(
                    ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(outputId)),
                    outputCount
            );

            int energyCost = GsonHelper.getAsInt(json, "energy_cost", 0);
            return new RuneBlackRecipe(id, input, output, energyCost);
        }

        @Override
        public RuneBlackRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            int energyCost = buffer.readInt();
            return new RuneBlackRecipe(id, input, output, energyCost);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RuneBlackRecipe recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeInt(recipe.energyCost);
        }
    }
}
