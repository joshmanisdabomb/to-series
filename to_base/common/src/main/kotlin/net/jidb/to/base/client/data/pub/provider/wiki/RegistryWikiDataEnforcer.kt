package net.jidb.to.base.client.data.pub.provider.wiki

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

class RegistryWikiDataEnforcer(val check: (ResourceKey<*>) -> Boolean) : WikiDataEnforcer {

    constructor(modid: String, except: (ResourceKey<*>) -> Boolean) : this({ it.identifier().namespace == modid && !except(it) })
    constructor(modid: String, except: List<ResourceKey<*>>) : this(modid, { except.contains(it) })
    constructor(modid: String) : this(modid, { false })

    override fun enforce(created: List<Pair<Identifier, Identifier>>): List<Pair<Identifier, Identifier>> {
        return BuiltInRegistries.REGISTRY.flatMap { it.registryKeySet().filter { check(it) }.map { it.registry() to it.identifier() }.filter { !created.contains(it) } }
    }

}