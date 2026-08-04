package net.jidb.to.base.fabric.client.platform

import net.jidb.to.base.client.api.platform.EntitiesClientPlatformModule
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

/**
 * [EntitiesClientPlatformModule] implementation for Fabric.
 *
 * @since 0.4.0
 */
object EntitiesFabricClientPlatformModule : EntitiesClientPlatformModule() {

    override fun <E : Entity> registerEntityRenderer(modid: String, type: () -> EntityType<E>, renderer: EntityRendererProvider<E>) {
        EntityRenderers.register(type(), renderer)
    }

}
