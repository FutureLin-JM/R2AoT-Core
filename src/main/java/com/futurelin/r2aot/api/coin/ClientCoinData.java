package com.futurelin.r2aot.api.coin;

public class ClientCoinData {
    private static long clientCoins = 0;

    public static long getClientCoins() {
        return clientCoins;
    }

    public static void setClientCoins(long coins) {
        clientCoins = coins;
    }
}