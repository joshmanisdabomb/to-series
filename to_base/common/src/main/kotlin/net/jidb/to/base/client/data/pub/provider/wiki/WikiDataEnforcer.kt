package net.jidb.to.base.client.data.pub.provider.wiki

import net.minecraft.resources.ResourceKey

interface WikiDataEnforcer {

    fun enforce(created: List<ResourceKey<*>>): List<ResourceKey<*>>

}