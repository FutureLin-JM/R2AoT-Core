package com.futurelin.r2aot.registry;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.common.block.RuneBlack;
import com.futurelin.r2aot.common.block.item.RuneBlackItem;
import com.futurelin.r2aot.common.block.tile.RuneBlackTile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.futurelin.r2aot.registry.ItemsRegistry.defaultItemProperties;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, R2AoTCore.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, R2AoTCore.MOD_ID);

    public static final RegistryObject<RuneBlack> RUNE_BLACK =
            registerBlockAndItem("rune_black", RuneBlack::new, (reg) -> new RuneBlackItem(reg, defaultItemProperties()));

    public static final RegistryObject<BlockEntityType<RuneBlackTile>> RUNE_BLACK_TILE =
            BLOCK_ENTITIES.register("rune_black", () ->
                    BlockEntityType.Builder.of(RuneBlackTile::new, RUNE_BLACK.get()).build(null));

    private static <T extends Block> RegistryObject<T> registerBlockAndItem(String name, Supplier<T> block) {
        RegistryObject<T> blocks = BLOCKS.register(name, block);
        ItemsRegistry.ITEMS.register(name, () -> new BlockItem(blocks.get(), defaultItemProperties()));
        return blocks;
    }

    private static <T extends Block, I extends BlockItem> RegistryObject<T> registerBlockAndItem(
            String name,
            Supplier<T> block,
            Function<T, I> itemFactory
    ) {
        RegistryObject<T> blocks = BLOCKS.register(name, block);
        ItemsRegistry.ITEMS.register(name, () -> itemFactory.apply(blocks.get()));
        return blocks;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
