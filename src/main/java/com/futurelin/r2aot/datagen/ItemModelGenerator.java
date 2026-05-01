package com.futurelin.r2aot.datagen;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, R2AoTCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ItemsRegistry.CHALK_BLACK.get());
        basicItem(ItemsRegistry.SILVER_COIN.get());
        basicItem(ItemsRegistry.GOLD_COIN.get());
        basicItem(ItemsRegistry.Crystal_COIN.get());
    }

    @Override
    public ItemModelBuilder basicItem(ResourceLocation item) {
        String path = item.getPath();
        String builderPath = path.contains("/") ? item.getNamespace() + ":" + "item/" + path : path;
        return getBuilder(builderPath)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + path));
    }
}
