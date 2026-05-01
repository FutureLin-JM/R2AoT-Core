package com.futurelin.r2aot.common.recipe;

import com.futurelin.r2aot.registry.RecipeRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.block.tile.EnchantingApparatusTile;
import com.hollingsworth.arsnouveau.common.util.Log;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import owmii.powah.block.thermo.ThermoBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnchantingChargeRecipe extends EnchantingApparatusRecipe {

    public int energyCost;

    public EnchantingChargeRecipe(ResourceLocation id, List<Ingredient> pedestalItems, Ingredient reagent, ItemStack result, int sourceCost, int energyCost) {
        super(id, pedestalItems, reagent, result, sourceCost, false);
        this.energyCost = energyCost;
    }

    @Override
    public boolean excludeJei() {
        return true;
    }

    @Override
    public boolean isMatch(List<ItemStack> pedestalItems, ItemStack reagent, EnchantingApparatusTile enchantingApparatusTile, @Nullable Player player) {
        BlockPos belowPos = enchantingApparatusTile.getBlockPos().below();
        BlockState belowState = enchantingApparatusTile.getLevel().getBlockState(belowPos);
        if (!(belowState.getBlock() instanceof ThermoBlock)) {
            return false;
        }
        return super.isMatch(pedestalItems, reagent, enchantingApparatusTile, player);
    }

    public boolean isMatchIgnoringThermo(List<ItemStack> pedestalItems, ItemStack reagent, EnchantingApparatusTile enchantingApparatusTile, @Nullable Player player) {
        return super.isMatch(pedestalItems, reagent, enchantingApparatusTile, player);
    }

    public int getEnergyCost() {
        return energyCost;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ENCHANTING_CHARGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.ENCHANTING_CHARGE_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<EnchantingChargeRecipe> {

        private List<Ingredient> getPedestalItems(ResourceLocation recipeId, JsonArray pedestalJson) {
            return StreamSupport.stream(pedestalJson.spliterator(), true).map(el -> {
                if (el instanceof JsonObject obj && (GsonHelper.isObjectNode(obj, "item") || GsonHelper.isArrayNode(obj, "item"))) {
                    Log.getLogger().warn("Use of deprecated recipe format in recipe ID: {}", recipeId);
                    return Ingredient.fromJson(obj.get("item"));
                }
                return Ingredient.fromJson(el);
            }).collect(Collectors.toList());
        }

        @Override
        public EnchantingChargeRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            JsonArray pedestalItemsJson = GsonHelper.getAsJsonArray(pSerializedRecipe, "pedestalItems");
            List<Ingredient> pedestalItems = getPedestalItems(pRecipeId, pedestalItemsJson);
            Ingredient reagent = Ingredient.fromJson(GsonHelper.getAsJsonArray(pSerializedRecipe, "reagent"));
            ItemStack output = ShapedRecipe.itemStackFromJson(pSerializedRecipe.getAsJsonObject("output"));
            int sourceCost = pSerializedRecipe.has("sourceCost") ? GsonHelper.getAsInt(pSerializedRecipe, "sourceCost") : 0;
            int energyCost = pSerializedRecipe.has("energyCost") ? GsonHelper.getAsInt(pSerializedRecipe, "energyCost") : 0;

            return new EnchantingChargeRecipe(pRecipeId, pedestalItems, reagent, output, sourceCost, energyCost);
        }

        @Override
        public @Nullable EnchantingChargeRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int pedestalCount = pBuffer.readVarInt();
            List<Ingredient> pedestalItems = new ArrayList<>();
            for (int i = 0; i < pedestalCount; i++) {
                pedestalItems.add(Ingredient.fromNetwork(pBuffer));
            }

            Ingredient reagent = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int sourceCost = pBuffer.readVarInt();
            int energyCost = pBuffer.readVarInt();
            
            return new EnchantingChargeRecipe(pRecipeId, pedestalItems, reagent, result, sourceCost, energyCost);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, EnchantingChargeRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.pedestalItems.size());
            for (Ingredient ingredient : pRecipe.pedestalItems) {
                ingredient.toNetwork(pBuffer);
            }

            pRecipe.reagent.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.sourceCost);
            pBuffer.writeVarInt(pRecipe.energyCost);
        }
    }
}
