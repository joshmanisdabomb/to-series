package net.jidb.to.base.data.provider.wiki

import net.minecraft.resources.ResourceKey

interface WikiDataEnforcer {

    fun enforce(created: List<ResourceKey<*>>): List<ResourceKey<*>>

}