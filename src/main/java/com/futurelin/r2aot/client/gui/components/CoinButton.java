package com.futurelin.r2aot.client.gui.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class CoinButton extends Button {

    private final AbstractContainerScreen<?> parentGui;
    private final int xOffsetNormal;
    private final int xOffsetCreative;
    private final int yOffset;
    private final ResourceLocation texture;
    private final ResourceLocation textureHighlighted;
    private final IntConsumer clickAction;
    private final Supplier<Boolean> shouldRender;

    public CoinButton(
            AbstractContainerScreen<?> parentGui,
            int xOffsetNormal, int xOffsetCreative, int yOffset,
            int width, int height,
            ResourceLocation texture,
            ResourceLocation textureHighlighted,
            IntConsumer clickAction,
            Supplier<Boolean> shouldRender,
            Component tooltipText
    ) {
        super(0, 0, width, height, Component.empty(), (btn) -> {}, Button.DEFAULT_NARRATION);
        this.parentGui = parentGui;
        this.xOffsetNormal = xOffsetNormal;
        this.xOffsetCreative = xOffsetCreative;
        this.yOffset = yOffset;
        this.texture = texture;
        this.textureHighlighted = textureHighlighted;
        this.clickAction = clickAction;
        this.shouldRender = shouldRender;
        this.setTooltip(Tooltip.create(tooltipText));
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (!shouldRender.get()) {
            return;
        }

        boolean isCreativeInventory = parentGui instanceof CreativeModeInventoryScreen;
        int xOffset = isCreativeInventory ? xOffsetCreative : xOffsetNormal;

        this.setX(parentGui.getGuiLeft() + xOffset);
        this.setY(parentGui.getGuiTop() + yOffset);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        ResourceLocation textureToUse = this.isHovered() ? textureHighlighted : texture;
        RenderSystem.setShaderTexture(0, textureToUse);

        pGuiGraphics.blit(textureToUse, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!shouldRender.get()) {
            return false;
        }

        if (!this.active || !this.visible || !this.clicked(pMouseX, pMouseY)) {
            return false;
        }

        if (pButton != InputConstants.MOUSE_BUTTON_LEFT && pButton != InputConstants.MOUSE_BUTTON_RIGHT) {
            return false;
        }

        this.playDownSound(Minecraft.getInstance().getSoundManager());
        this.clickAction.accept(pButton);
        return true;
    }

    @Override
    public void playDownSound(SoundManager pHandler) {
        pHandler.play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.25F));
    }
}
