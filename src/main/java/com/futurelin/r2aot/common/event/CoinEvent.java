package com.futurelin.r2aot.common.event;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.api.coin.CoinData;
import com.futurelin.r2aot.network.Networking;
import com.futurelin.r2aot.network.packet.SyncCoinDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = R2AoTCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CoinEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.level().isClientSide) {
                long coin = CoinData.getCoins(serverPlayer);
                Networking.sendToPlayer(new SyncCoinDataPacket(coin), serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            CoinData.setCoins(event.getEntity(), CoinData.getCoins(event.getOriginal()));
            if (event.getEntity() instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide) {
                long coin = CoinData.getCoins(serverPlayer);
                Networking.sendToPlayer(new SyncCoinDataPacket(coin), serverPlayer);
            }
        }
    }
}
