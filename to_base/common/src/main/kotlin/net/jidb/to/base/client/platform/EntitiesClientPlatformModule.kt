package net.jidb.to.base.client.platform

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

abstract class EntitiesClientPlatformModule {

    abstract fun <E : Entity> registerEntityRenderer(modid: String, type: () -> EntityType<E>, renderer: EntityRendererProvider<E>)

}
