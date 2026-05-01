package com.futurelin.r2aot;

import com.futurelin.r2aot.client.render.entity.RitualmasterRenderer;
import com.futurelin.r2aot.common.entity.RitualmasterEntity;
import com.futurelin.r2aot.network.Networking;
import com.futurelin.r2aot.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(R2AoTCore.MOD_ID)
public class R2AoTCore {

    public static final String MOD_ID = "r2aot";
    public static final Logger LOGGER = LogUtils.getLogger();

    public R2AoTCore(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onEntityAttributeCreation);

        ItemsRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        CreativeModTabs.register(modEventBus);
        RecipeRegistry.register(modEventBus);
        EntitiesRegistry.register(modEventBus);
        JobsRegistry.register(modEventBus);

        GeckoLib.initialize();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        Networking.register();
        RecipeRegistry.postInit();
    }

    private void onEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(EntitiesRegistry.RITUALMASTER.get(), RitualmasterEntity.createAttributes().build());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntitiesRegistry.RITUALMASTER.get(), RitualmasterRenderer::new);
        }
    }
}
