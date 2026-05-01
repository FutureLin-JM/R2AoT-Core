package com.futurelin.r2aot.client.gui.components;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.api.coin.ClientCoinData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.resources.ResourceLocation;

import java.text.NumberFormat;
import java.util.Locale;

public class BalanceDisplay {

    private static final int DISPLAY_WIDTH = 96;
    private static final int DISPLAY_HEIGHT = 24;
    private static final ResourceLocation BALANCE_DISPLAY_TEXTURE = ResourceLocation.tryParse(R2AoTCore.MOD_ID + ":textures/gui/balance_display.png");

    public static void render(GuiGraphics guiGraphics, AbstractContainerScreen<?> parentGui, int yOffset) {
        if (parentGui instanceof CreativeModeInventoryScreen creativeScreen) {
            if (!creativeScreen.isInventoryOpen()) {
                return;
            }
        }

        int displayX = parentGui.getGuiLeft();
        int displayY = parentGui.getGuiTop() + yOffset;

        drawBackground(guiGraphics, displayX, displayY);
        drawBalanceText(guiGraphics, displayX, displayY);
    }

    private static void drawBackground(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(BALANCE_DISPLAY_TEXTURE, x, y, 0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, DISPLAY_WIDTH, DISPLAY_HEIGHT);
    }

    private static void drawBalanceText(GuiGraphics guiGraphics, int displayX, int displayY) {
        String balanceText = formatCoins(ClientCoinData.getClientCoins());
        int fontWidth = Minecraft.getInstance().font.width(balanceText);
        float textX = displayX + DISPLAY_WIDTH - 6 - fontWidth;
        float textY = displayY + (float) (DISPLAY_HEIGHT - 8) / 2;

        guiGraphics.drawString(Minecraft.getInstance().font, balanceText, textX, textY, ChatFormatting.GOLD.getColor(), true);
    }

    private static String formatCoins(long coins) {
        return NumberFormat.getNumberInstance(Locale.US).format(coins);
    }
}
