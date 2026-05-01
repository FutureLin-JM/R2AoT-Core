package com.futurelin.r2aot.registry;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.common.entity.RitualmasterEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntitiesRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
            .create(ForgeRegistries.ENTITY_TYPES, R2AoTCore.MOD_ID);

    public static final RegistryObject<EntityType<RitualmasterEntity>> RITUALMASTER = ENTITY_TYPES.register(
            "ritualmaster",
            () -> EntityType.Builder.of(RitualmasterEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.2f)
                    .clientTrackingRange(8)
                    .build("ritualmaster"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
