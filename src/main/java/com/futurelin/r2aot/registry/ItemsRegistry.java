package com.futurelin.r2aot.registry;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.api.coin.CoinData;
import com.futurelin.r2aot.common.item.ChalkBlack;
import com.futurelin.r2aot.common.item.CoinItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, R2AoTCore.MOD_ID);

    public static final RegistryObject<Item> CHALK_BLACK =
            ITEMS.register("chalk_black",
                    () -> new ChalkBlack(defaultItemProperties().stacksTo(1).durability(10).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> SILVER_COIN =
            ITEMS.register("silver_coin", () -> new CoinItem(defaultItemProperties(), CoinData.SILVER_COIN_VALUE, Rarity.UNCOMMON));

    public static final RegistryObject<Item> GOLD_COIN =
            ITEMS.register("gold_coin", () -> new CoinItem(defaultItemProperties(), CoinData.GOLD_COIN_VALUE, Rarity.RARE));

    public static final RegistryObject<Item> Crystal_COIN =
            ITEMS.register("crystal_coin", () -> new CoinItem(defaultItemProperties(), CoinData.CRYSTAL_COIN_VALUE, Rarity.EPIC));

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties();
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
