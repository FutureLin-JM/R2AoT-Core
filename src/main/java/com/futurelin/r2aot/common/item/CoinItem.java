package com.futurelin.r2aot.common.item;

import com.futurelin.r2aot.TranslationKeys;
import com.futurelin.r2aot.api.coin.CoinData;
import com.futurelin.r2aot.api.item.AbstractBaseItemWithTooltip;
import com.futurelin.r2aot.network.Networking;
import com.futurelin.r2aot.network.packet.SyncCoinDataPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoinItem extends AbstractBaseItemWithTooltip {
    private final int coinValue;

    public CoinItem(Properties pProperties, int coinValue, Rarity rarity) {
        super(pProperties.fireResistant().rarity(rarity));
        this.coinValue = coinValue;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    public int getCoinValue() {
        return coinValue;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer serverPlayer) {
            handleDepositRequest(pLevel, serverPlayer, pUsedHand);
        }

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }

    public static void handleDepositRequest(Level level, ServerPlayer player, InteractionHand usedHand) {
        ItemStack usedStack = player.getItemInHand(usedHand);
        if (!(usedStack.getItem() instanceof CoinItem)) {
            return;
        }

        long depositedValue = player.isShiftKeyDown() ? depositAllCoins(player) : depositCoinStack(player, usedStack);
        if (depositedValue <= 0) {
            return;
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1, 1);
        player.displayClientMessage(
                Component.translatable(TranslationKeys.MESSAGE_COIN_DEPOSIT, depositedValue).withStyle(ChatFormatting.GOLD),
                true
        );
        Networking.sendToPlayer(new SyncCoinDataPacket(CoinData.getCoins(player)), player);
    }

    private static long depositAllCoins(ServerPlayer player) {
        long depositedValue = 0;

        for (ItemStack stack : player.getInventory().items) {
            depositedValue += depositCoinStack(player, stack);
        }

        for (ItemStack stack : player.getInventory().offhand) {
            depositedValue += depositCoinStack(player, stack);
        }

        return depositedValue;
    }

    private static long depositCoinStack(ServerPlayer player, ItemStack stack) {
        if (!(stack.getItem() instanceof CoinItem coinItem) || stack.isEmpty()) {
            return 0;
        }

        int amount = stack.getCount();
        long depositedValue = (long) coinItem.getCoinValue() * amount;
        CoinData.addCoins(player, depositedValue);
        stack.shrink(amount);
        return depositedValue;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        tooltips.add(Component.translatable(TranslationKeys.TOOLTIP_COIN_VALUE, coinValue).withStyle(ChatFormatting.GRAY));
    }
}
