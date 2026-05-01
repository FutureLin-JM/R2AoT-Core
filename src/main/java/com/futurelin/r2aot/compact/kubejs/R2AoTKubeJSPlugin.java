package com.futurelin.r2aot.compact.kubejs;

import com.futurelin.r2aot.compact.kubejs.recipes.EnchantingChargeRecipeJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public class R2AoTKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace("r2aot")
                .register("enchanting_charge", EnchantingChargeRecipeJS.SCHEMA);
    }
}
