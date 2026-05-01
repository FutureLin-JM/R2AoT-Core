package com.futurelin.r2aot.network.packet;

import com.futurelin.r2aot.api.coin.ClientCoinData;
import com.futurelin.r2aot.network.PacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncCoinDataPacket extends PacketBase {
    private long coins;

    public SyncCoinDataPacket(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    public SyncCoinDataPacket(long coins) {
        this.coins = coins;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(coins);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.coins = buf.readLong();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player, NetworkEvent.Context context) {
        ClientCoinData.setClientCoins(coins);
    }
}
