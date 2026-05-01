package com.futurelin.r2aot.network.packet;

import com.futurelin.r2aot.api.coin.CoinData;
import com.futurelin.r2aot.network.Networking;
import com.futurelin.r2aot.network.PacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class WithdrawCoinPacket extends PacketBase {
    private int coinValue;
    private int amount;

    public WithdrawCoinPacket(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    public WithdrawCoinPacket(int coinValue, int amount) {
        this.coinValue = coinValue;
        this.amount = amount;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(coinValue);
        buf.writeInt(amount);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.coinValue = buf.readInt();
        this.amount = buf.readInt();
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player, NetworkEvent.Context context) {
        if (amount <= 0 || CoinData.getCoinItem(coinValue) == null) {
            return;
        }

        long totalValue = (long) coinValue * amount;
        if (CoinData.removeCoins(player, totalValue)) {
            ItemStack stack = CoinData.getCoinStack(coinValue, amount);
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
            Networking.sendToPlayer(new SyncCoinDataPacket(CoinData.getCoins(player)), player);
        }
    }
}
