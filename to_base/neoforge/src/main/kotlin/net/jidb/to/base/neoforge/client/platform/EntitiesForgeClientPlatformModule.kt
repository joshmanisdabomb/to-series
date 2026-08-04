package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.EntitiesClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent

/**
 * [EntitiesClientPlatformModule] implementation for Neoforge.
 *
 * A renderer cannot be registered whenever a mod asks, only while Neoforge is raising its own event, so every declaration is queued in a [DeferredForgeEventRegistry] and played back once that arrives.
 *
 * @since 0.4.0
 */
object EntitiesForgeClientPlatformModule : EntitiesClientPlatformModule() {

    /**
     * The queued registrations, played back when Neoforge raises the event they are waiting on.
     *
     * @since 0.4.0
     */
    val registry = DeferredForgeEventRegistry(EntityRenderersEvent.RegisterRenderers::class.java)

    override fun <E : Entity> registerEntityRenderer(modid: String, type: () -> EntityType<E>, renderer: EntityRendererProvider<E>) {
        registry.register(modid) { event ->
            event.registerEntityRenderer(type(), renderer)
        }
    }

}
