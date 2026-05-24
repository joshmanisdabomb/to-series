package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.platform.EntitiesClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent

object EntitiesForgeClientPlatformModule : EntitiesClientPlatformModule() {

    val registry = DeferredForgeEventRegistry(EntityRenderersEvent.RegisterRenderers::class.java)

    override fun <E : Entity> registerEntityRenderer(modid: String, type: () -> EntityType<E>, renderer: EntityRendererProvider<E>) {
        registry.register(modid) { event ->
            event.registerEntityRenderer(type(), renderer)
        }
    }

}
