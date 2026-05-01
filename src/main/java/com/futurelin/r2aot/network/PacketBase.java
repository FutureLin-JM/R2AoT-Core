package com.futurelin.r2aot.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public abstract class PacketBase implements IPacket {
    protected PacketBase() {
    }

    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft minecraft, Player player, NetworkEvent.Context context) {
    }

    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player, NetworkEvent.Context context) {
    }
}
