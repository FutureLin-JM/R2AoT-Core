package com.futurelin.r2aot.client.gui;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.TranslationKeys;
import com.futurelin.r2aot.api.coin.ClientCoinData;
import com.futurelin.r2aot.api.coin.CoinData;
import com.futurelin.r2aot.client.gui.components.BalanceDisplay;
import com.futurelin.r2aot.client.gui.components.CoinButton;
import com.futurelin.r2aot.network.Networking;
import com.futurelin.r2aot.network.packet.WithdrawCoinPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.client.gui.CuriosScreen;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = R2AoTCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryCoinHandler {

    private static final int BUTTON_SIZE = 13;

    private static final ResourceLocation SILVER_BUTTON = ResourceLocation.tryParse(R2AoTCore.MOD_ID + ":textures/gui/silver_button.png");
    private static final ResourceLocation SILVER_BUTTON_HIGHLIGHTED = ResourceLocation.tryParse(R2AoTCore.MOD_ID + ":textures/gui/silver_button_highlighted.png");
    private static final ResourceLocation GOLD_BUTTON = ResourceLocation.tryParse(R2AoTCore.MOD_ID + ":textures/gui/gold_button.png");
    private static final ResourceLocation GOLD_BUTTON_HIGHLIGHTED = ResourceLocation.tryParse(R2AoTCore.MOD_ID + ":textures/gui/gold_button_highlighted.png");
    private static final ResourceLocation CRYSTAL_BUTTON = ResourceLocation.tryParse(R2AoTCore.MOD_ID + ":textures/gui/crystal_button.png");
    private static final ResourceLocation CRYSTAL_BUTTON_HIGHLIGHTED = ResourceLocation.tryParse(R2AoTCore.MOD_ID + ":textures/gui/crystal_button_highlighted.png");

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        boolean isInventory = screen instanceof InventoryScreen;
        boolean isCreative = screen instanceof CreativeModeInventoryScreen;
        boolean isCurios = screen instanceof CuriosScreen;

        if (isInventory || isCreative || isCurios) {
            addCoinButtons(event, (AbstractContainerScreen<?>) screen);
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        boolean isInventory = screen instanceof InventoryScreen;
        boolean isCreative = screen instanceof CreativeModeInventoryScreen;
        boolean isCurios = screen instanceof CuriosScreen;

        if (isInventory || isCreative || isCurios) {
            int yOffset = isCreative ? -75 : -28;
            BalanceDisplay.render(event.getGuiGraphics(), (AbstractContainerScreen<?>) screen, yOffset);
        }
    }

    private static void addCoinButtons(ScreenEvent.Init.Post event, AbstractContainerScreen<?> inventoryScreen) {
        Supplier<Boolean> shouldRender = () -> {
            if (inventoryScreen instanceof CreativeModeInventoryScreen creativeScreen) {
                return creativeScreen.isInventoryOpen();
            }
            return true;
        };

        addSilverButton(event, inventoryScreen, shouldRender);
        addGoldButton(event, inventoryScreen, shouldRender);
        addCrystalButton(event, inventoryScreen, shouldRender);
    }

    private static void addSilverButton(ScreenEvent.Init.Post event, AbstractContainerScreen<?> inventoryScreen, Supplier<Boolean> shouldRender) {
        CoinButton button = new CoinButton(
                inventoryScreen, 77, 127, getSilverYOffset(inventoryScreen),
                BUTTON_SIZE, BUTTON_SIZE,
                SILVER_BUTTON, SILVER_BUTTON_HIGHLIGHTED,
                (mouseButton) -> withdrawCoin(CoinData.SILVER_COIN_VALUE, mouseButton),
                shouldRender,
                Component.translatable(TranslationKeys.TOOLTIP_COIN_WITHDRAW, CoinData.SILVER_COIN_VALUE).withStyle(ChatFormatting.GRAY)
        );
        event.addListener(button);
    }

    private static void addGoldButton(ScreenEvent.Init.Post event, AbstractContainerScreen<?> inventoryScreen, Supplier<Boolean> shouldRender) {
        CoinButton button = new CoinButton(
                inventoryScreen, 77, 127, getGoldYOffset(inventoryScreen),
                BUTTON_SIZE, BUTTON_SIZE,
                GOLD_BUTTON, GOLD_BUTTON_HIGHLIGHTED,
                (mouseButton) -> withdrawCoin(CoinData.GOLD_COIN_VALUE, mouseButton),
                shouldRender,
                Component.translatable(TranslationKeys.TOOLTIP_COIN_WITHDRAW, CoinData.GOLD_COIN_VALUE).withStyle(ChatFormatting.GRAY)
        );
        event.addListener(button);
    }

    private static void addCrystalButton(ScreenEvent.Init.Post event, AbstractContainerScreen<?> inventoryScreen, Supplier<Boolean> shouldRender) {
        CoinButton button = new CoinButton(
                inventoryScreen, 77, 127, getCrystalYOffset(inventoryScreen),
                BUTTON_SIZE, BUTTON_SIZE,
                CRYSTAL_BUTTON, CRYSTAL_BUTTON_HIGHLIGHTED,
                (mouseButton) -> withdrawCoin(CoinData.CRYSTAL_COIN_VALUE, mouseButton),
                shouldRender,
                Component.translatable(TranslationKeys.TOOLTIP_COIN_WITHDRAW, CoinData.CRYSTAL_COIN_VALUE).withStyle(ChatFormatting.GRAY)
        );
        event.addListener(button);
    }

    private static void withdrawCoin(int coinValue, int mouseButton) {
        int amount = resolveWithdrawAmount(coinValue, mouseButton);
        if (amount > 0) {
            Networking.sendToServer(new WithdrawCoinPacket(coinValue, amount));

            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.5f, 1.0f);
            }
        } else {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.displayClientMessage(
                        Component.translatable(TranslationKeys.MESSAGE_NOT_WITHDRAW),
                        true
                );
            }
        }
    }

    private static int resolveWithdrawAmount(int coinValue, int mouseButton) {
        long clientCoins = ClientCoinData.getClientCoins();
        if (coinValue <= 0 || clientCoins < coinValue) {
            return 0;
        }

        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return 1;
        }

        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            return (int) Math.min(64, clientCoins / coinValue);
        }

        return 0;
    }


    private static int getSilverYOffset(AbstractContainerScreen<?> screen) {
        return screen instanceof CreativeModeInventoryScreen ? 5 : 18;
    }

    private static int getGoldYOffset(AbstractContainerScreen<?> screen) {
        return getSilverYOffset(screen) + (screen instanceof CreativeModeInventoryScreen ? 16 : 14);
    }

    private static int getCrystalYOffset(AbstractContainerScreen<?> screen) {
        return getGoldYOffset(screen) + (screen instanceof CreativeModeInventoryScreen ? 16 : 14);
    }
}
