package com.futurelin.r2aot.client.model.entity;

import com.futurelin.r2aot.R2AoTCore;
import com.futurelin.r2aot.common.entity.RitualmasterEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RitualmasterModel extends DefaultedEntityGeoModel<RitualmasterEntity> {

    public RitualmasterModel() {
        super(ResourceLocation.fromNamespaceAndPath(R2AoTCore.MOD_ID, "ritualmaster"));
    }
}
