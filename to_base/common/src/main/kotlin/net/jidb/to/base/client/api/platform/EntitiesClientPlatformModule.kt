package net.jidb.to.base.client.api.platform

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

/**
 * A [ClientPlatform]-specific module that defines a cross-platform contract for handling entities.
 * This module has code for registering an [EntityRendererProvider].
 *
 * @since 0.4.0
 */
abstract class EntitiesClientPlatformModule {

    /**
     * Registers an [EntityRendererProvider] for a specific [EntityType].
     *
     * @param E The type of the entity.
     * @param modid The unique namespace of the mod registering the renderer.
     * @param type An [EntityType] supplier that the renderer is bound to.
     * @param renderer The provider responsible for creating the [net.minecraft.client.renderer.entity.EntityRenderer].
     * @since 0.4.0
     */
    abstract fun <E : Entity> registerEntityRenderer(modid: String, type: () -> EntityType<E>, renderer: EntityRendererProvider<E>)

}
