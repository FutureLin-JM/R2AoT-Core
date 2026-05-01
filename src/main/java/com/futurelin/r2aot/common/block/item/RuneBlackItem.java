package com.futurelin.r2aot.common.block.item;

import com.futurelin.r2aot.api.item.AbstractBaseBlockItemWithTooltip;
import com.futurelin.r2aot.api.item.KeyType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RuneBlackItem extends AbstractBaseBlockItemWithTooltip {

    public RuneBlackItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        addTooltipWhileKeyDown(KeyType.SHIFT, tooltips, stack, 3);
    }
}
