package com.futurelin.r2aot.api.coin;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.registry.ItemsRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CoinData {
    public static final String COIN_KEY = R2AoTCore.MOD_ID + "_coin";
    public static final int SILVER_COIN_VALUE = 10;
    public static final int GOLD_COIN_VALUE = 100;
    public static final int CRYSTAL_COIN_VALUE = 1000;

    public static long getCoins(Player player) {
        CompoundTag data = player.getPersistentData();
        return data.getLong(COIN_KEY);
    }

    public static void setCoins(Player player, long coins) {
        CompoundTag data = player.getPersistentData();
        data.putLong(COIN_KEY, Math.max(0, coins));
    }

    public static void addCoins(Player player, long coins) {
        setCoins(player, getCoins(player) + coins);
    }

    public static boolean removeCoins(Player player, long coins) {
        long currentCoins = getCoins(player);
        if (currentCoins >= coins) {
            setCoins(player, currentCoins - coins);
            return true;
        }
        return false;
    }

    public static ItemStack getCoinStack(int coinValue, int amount) {
        Item coinItem = getCoinItem(coinValue);
        if (coinItem == null || amount <= 0) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(coinItem, amount);
    }

    public static Item getCoinItem(int value) {
        return switch (value) {
            case SILVER_COIN_VALUE -> ItemsRegistry.SILVER_COIN.get();
            case GOLD_COIN_VALUE -> ItemsRegistry.GOLD_COIN.get();
            case CRYSTAL_COIN_VALUE -> ItemsRegistry.Crystal_COIN.get();
            default -> null;
        };
    }

}

