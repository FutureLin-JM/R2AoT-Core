package com.futurelin.r2aot.registry;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.common.entity.job.RitualmasterJob;
import com.klikli_dev.occultism.client.entities.SpiritJobClient;
import com.klikli_dev.occultism.common.entity.job.SpiritJobFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class JobsRegistry {

        public static final DeferredRegister<SpiritJobFactory> JOBS = DeferredRegister
                        .create(ResourceLocation.fromNamespaceAndPath("occultism", "spirit_job_factory"), R2AoTCore.MOD_ID);

        public static final RegistryObject<SpiritJobFactory> RITUAL_MASTER = JOBS.register("ritualmaster",
                        () -> new SpiritJobFactory(
                                        RitualmasterJob::new,
                                        SpiritJobClient.create()));

        public static void register(IEventBus eventBus) {
                JOBS.register(eventBus);
        }
}
