package com.futurelin.r2aot.registry;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.common.recipe.EnchantingChargeRecipe;
import com.futurelin.r2aot.common.recipe.RuneBlackRecipe;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, R2AoTCore.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, R2AoTCore.MOD_ID);

    public static final String RUNE_BLACK_RECIPE_ID = "rune_black";
    public static final String ENCHANTING_CHARGE_RECIPE_ID = "enchanting_charge";

    public static final RegistryObject<RecipeType<RuneBlackRecipe>> RUNE_BLACK_TYPE = RECIPE_TYPES.register(RUNE_BLACK_RECIPE_ID, ModRecipeType::new);
    public static final RegistryObject<RecipeSerializer<RuneBlackRecipe>> RUNE_BLACK_SERIALIZER = SERIALIZERS.register(RUNE_BLACK_RECIPE_ID, RuneBlackRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<EnchantingChargeRecipe>> ENCHANTING_CHARGE_RECIPE = RECIPE_TYPES.register(ENCHANTING_CHARGE_RECIPE_ID, ModRecipeType::new);
    public static final RegistryObject<RecipeSerializer<EnchantingChargeRecipe>> ENCHANTING_CHARGE_SERIALIZER = SERIALIZERS.register(ENCHANTING_CHARGE_RECIPE_ID, EnchantingChargeRecipe.Serializer::new);

    private static class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
        @Override
        public String toString() {
            return ForgeRegistries.RECIPE_TYPES.getKey(this).toString();
        }
    }

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }

    public static void postInit() {
        ArsNouveauAPI.getInstance().getEnchantingRecipeTypes().add(ENCHANTING_CHARGE_RECIPE.get());
    }
}
