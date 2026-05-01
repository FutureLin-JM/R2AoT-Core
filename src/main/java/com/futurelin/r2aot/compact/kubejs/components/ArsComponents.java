package com.futurelin.r2aot.compact.kubejs.components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentWithParent;
import dev.latvian.mods.kubejs.util.MapJS;

public class ArsComponents {

    public static RecipeComponent<InputItem> INPUT_ITEM = new RecipeComponentWithParent<InputItem>() {
        @Override
        public RecipeComponent<InputItem> parentComponent() {
            return ItemComponents.INPUT;
        }

        @Override
        public JsonElement write(RecipeJS recipe, InputItem value) {
            var json = new JsonObject();
            json.add("item", recipe.writeInputItem(value));
            return json;
        }

        @Override
        public InputItem read(RecipeJS recipe, Object from) {
            var json = MapJS.json(from);
            if (json == null) {
                return recipe.readInputItem(from);
            }
            JsonElement item = json.get("item");
            return recipe.readInputItem(item == null ? from : item);
        }
    };
}
