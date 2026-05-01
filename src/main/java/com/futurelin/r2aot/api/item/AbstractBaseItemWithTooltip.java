package com.futurelin.r2aot.api.item;

import com.futurelin.r2aot.TranslationKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractBaseItemWithTooltip extends Item {

    public AbstractBaseItemWithTooltip(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public abstract void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag);
    private boolean getKeyType(KeyType keyType) {
        return switch (keyType) {
            case SHIFT -> Screen.hasShiftDown();
            case ALT -> Screen.hasAltDown();
            case CONTROL -> Screen.hasControlDown();
        };
    }

    private Component buildHoldKeyTooltip(KeyType keyType, ChatFormatting lineStyle, ChatFormatting keyStyle) {
        return Component.translatable(
                TranslationKeys.TOOLTIP_HOLD,
                Component.literal(keyType.getValue()).withStyle(keyStyle)
        ).withStyle(lineStyle);
    }

    //Multiple
    public void addTooltip(List<Component> tooltips, ItemStack itemStack, int line) {
        for (int index = 1; index <= line; index++) {
            tooltips.add(Component.translatable(itemIdToKey(itemStack, index)));
        }
    }

    public void addTooltip(List<Component> tooltips, ItemStack itemStack, ChatFormatting style, int line, Object... obj) {
        for (int index = 1; index <= line; index++) {
            tooltips.add(Component.translatable(itemIdToKey(itemStack, index), obj).withStyle(style));
        }
    }

    public void addTooltip(List<Component> tooltips, ItemStack itemStack, ChatFormatting style, int line) {
        for (int index = 1; index <= line; index++) {
            tooltips.add(Component.translatable(itemIdToKey(itemStack, index)).withStyle(style));
        }
    }

    public String itemIdToKey(ItemStack itemStack, int index) {
        String rawKey = itemStack.getDescriptionId();
        return "tooltip" + rawKey.substring(rawKey.indexOf(".")) + "_" + index;
    }

    public void addTooltipWhileKeyDown(KeyType keyType, List<Component> tooltips, ItemStack itemStack, int line) {
        if (getKeyType(keyType)) {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.DARK_GRAY, ChatFormatting.WHITE));
            addTooltip(tooltips, itemStack, ChatFormatting.YELLOW, line);
        } else {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.GOLD, ChatFormatting.YELLOW));
        }
    }

    public void addTooltipWhileKeyDown(KeyType keyType, List<Component> tooltips, ItemStack itemStack, ChatFormatting style, int line, Object... obj) {
        if (getKeyType(keyType)) {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.DARK_GRAY, ChatFormatting.WHITE));
            addTooltip(tooltips, itemStack, style, line, obj);
        } else {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.GOLD, ChatFormatting.YELLOW));
        }
    }

    public void addTooltipWhileKeyDown(KeyType keyType, List<Component> tooltips, ItemStack itemStack, ChatFormatting style, int line) {
        if (getKeyType(keyType)) {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.DARK_GRAY, ChatFormatting.WHITE));
            addTooltip(tooltips, itemStack, style, line);
        } else {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.GOLD, ChatFormatting.YELLOW));
        }
    }

    //Single
    public void addTooltip(List<Component> tooltips, ItemStack itemStack) {
        tooltips.add(Component.translatable(itemIdToKey(itemStack)));
    }

    public void addTooltip(List<Component> tooltips, ItemStack itemStack, ChatFormatting style, Object... obj) {
        tooltips.add(Component.translatable(itemIdToKey(itemStack), obj).withStyle(style));
    }

    public void addTooltip(List<Component> tooltips, ItemStack itemStack, ChatFormatting style) {
        tooltips.add(Component.translatable(itemIdToKey(itemStack)).withStyle(style));
    }

    public String itemIdToKey(ItemStack itemStack) {
        String rawKey = itemStack.getDescriptionId();
        return "tooltip" + rawKey.substring(rawKey.indexOf("."));
    }

    public void addTooltipWhileKeyDown(KeyType keyType, List<Component> tooltips, ItemStack itemStack) {
        if (getKeyType(keyType)) {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.DARK_GRAY, ChatFormatting.WHITE));
            addTooltip(tooltips, itemStack);
        } else {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.GOLD, ChatFormatting.YELLOW));
        }
    }

    public void addTooltipWhileKeyDown(KeyType keyType, List<Component> tooltips, ItemStack itemStack, ChatFormatting style, Object... obj) {
        if (getKeyType(keyType)) {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.DARK_GRAY, ChatFormatting.WHITE));
            addTooltip(tooltips, itemStack, style, obj);
        } else {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.GOLD, ChatFormatting.YELLOW));
        }
    }

    public void addTooltipWhileKeyDown(KeyType keyType, List<Component> tooltips, ItemStack itemStack, ChatFormatting style) {
        if (getKeyType(keyType)) {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.DARK_GRAY, ChatFormatting.WHITE));
            addTooltip(tooltips, itemStack, style);
        } else {
            tooltips.add(buildHoldKeyTooltip(keyType, ChatFormatting.GOLD, ChatFormatting.YELLOW));
        }
    }
}