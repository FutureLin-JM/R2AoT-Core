package com.futurelin.r2aot.network;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.network.packet.SyncCoinDataPacket;
import com.futurelin.r2aot.network.packet.WithdrawCoinPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
    private static final String PROTOCOL_VERSION = "1.0";

    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(R2AoTCore.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void register() {

        INSTANCE.registerMessage(nextID(),
                WithdrawCoinPacket.class,
                WithdrawCoinPacket::encode,
                WithdrawCoinPacket::new,
                PacketHandler::handle
        );

        INSTANCE.registerMessage(nextID(),
                SyncCoinDataPacket.class,
                SyncCoinDataPacket::encode,
                SyncCoinDataPacket::new,
                PacketHandler::handle
        );
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
