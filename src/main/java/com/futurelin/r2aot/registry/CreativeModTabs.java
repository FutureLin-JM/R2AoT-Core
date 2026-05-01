package com.futurelin.r2aot.registry;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.TranslationKeys;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, R2AoTCore.MOD_ID);

    private static final Set<String> EXCLUDED_ITEMS = new HashSet<>(Arrays.asList(
        "r2aot:rune_black"
    ));

    public static final RegistryObject<CreativeModeTab> R2AOT_TAB =
        CREATIVE_MODE_TABS.register("r2aot_tab", () -> CreativeModeTab.builder()
                .icon(() -> new ItemStack(ItemsRegistry.GOLD_COIN.get()))
                .title(Component.translatable(TranslationKeys.ITEM_GROUP))
                .displayItems((pParameters, pOutput) -> {
                    ItemsRegistry.ITEMS.getEntries().forEach(item -> {
                        if (!isExcluded(item)) {
                            pOutput.accept(item.get());
                        }
                    });
                })
                .withTabsBefore(ResourceLocation.fromNamespaceAndPath("kubejs", "tab"))
                .build()
        );

    private static boolean isExcluded(RegistryObject<Item> item) {
        return EXCLUDED_ITEMS.contains(item.getId().toString());
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
