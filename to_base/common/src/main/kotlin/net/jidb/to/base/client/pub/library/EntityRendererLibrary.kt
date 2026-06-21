package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

open class EntityRendererLibrary(modid: String) : SimpleLibrary<EntityRendererLibrary.EntityRendererEntry<out Entity>>(modid) {

    override fun afterBuild(entry: Library<EntityRendererEntry<out Entity>, EntityRendererEntry<out Entity>>.LibraryEntry<out EntityRendererEntry<out Entity>, out EntityRendererEntry<out Entity>>) = entry.value.register()

    inner class EntityRendererEntry<E : Entity>(private val type: () -> EntityType<E>, private val provider: EntityRendererProvider<E>) {
        internal fun register() = ClientServices.platform.entities.registerEntityRenderer(modid, type, provider)
    }

}
