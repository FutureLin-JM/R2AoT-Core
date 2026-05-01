package com.futurelin.r2aot.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHandler {
    public static <T extends IPacket> void handle(T message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ctx.get().enqueueWork(() -> handleServer(message, ctx));
        } else {
            ctx.get().enqueueWork(() -> handleClient(message, ctx));
        }
        ctx.get().setPacketHandled(true);
    }

    public static <T extends IPacket> void handleServer(T message, Supplier<NetworkEvent.Context> ctx) {
        MinecraftServer server = ctx.get().getSender().level().getServer();
        message.onServerReceived(server, ctx.get().getSender(), ctx.get());
    }

    @OnlyIn(Dist.CLIENT)
    public static <T extends IPacket> void handleClient(T message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft minecraft = Minecraft.getInstance();
        message.onClientReceived(minecraft, minecraft.player, ctx.get());
    }
}
