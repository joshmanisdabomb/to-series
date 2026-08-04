package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

/**
 * [SimpleLibrary] implementation that registers an [EntityRendererProvider] against the [EntityType] it draws, and provides access to those renderers in one place.
 * The entity types themselves live in a common library, and are taken here as suppliers so that a renderer can be declared without the type having been built yet.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.4.0
 */
open class EntityRendererLibrary(modid: String) : SimpleLibrary<EntityRendererLibrary.EntityRendererEntry<out Entity>>(modid) {

    override fun afterBuild(entry: Library<EntityRendererEntry<out Entity>, EntityRendererEntry<out Entity>>.LibraryEntry<out EntityRendererEntry<out Entity>, out EntityRendererEntry<out Entity>>) = entry.value.register()

    /**
     * A renderer declared in the outer [EntityRendererLibrary], pairing the type of entity it draws with the provider that builds it.
     * The pair is kept in an entry of its own rather than registered directly, so that the entity's type parameter survives the wildcard the library stores.
     *
     * @param E The type of the entity being rendered.
     * @param type An [EntityType] supplier that the renderer is bound to.
     * @param provider The provider responsible for creating the [net.minecraft.client.renderer.entity.EntityRenderer].
     * @since 0.4.0
     */
    inner class EntityRendererEntry<E : Entity>(private val type: () -> EntityType<E>, private val provider: EntityRendererProvider<E>) {

        /**
         * Registers this renderer with the client platform, which the outer library does once the entry has been built.
         *
         * @return [Unit]
         * @since 0.4.0
         */
        internal fun register() = ClientServices.platform.entities.registerEntityRenderer(modid, type, provider)

    }

}
