package net.jidb.to.base.helper

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import kotlin.jvm.optionals.getOrNull

object RegistryHelper {

    fun splitResourceKey(key: String): Pair<Identifier, Identifier> {
        val split = key.split(" / ")
        return Identifier.parse(split[0]) to Identifier.parse(split[1])
    }

    fun createResourceKey(registry: Identifier, identifier: Identifier): ResourceKey<*>? {
        val registry = BuiltInRegistries.REGISTRY.getOptional(registry).getOrNull() ?: return null
        return registry.get(identifier).getOrNull()?.key()
    }
    fun createResourceKey(identifiers: Pair<Identifier, Identifier>) = createResourceKey(identifiers.first, identifiers.second)
    fun createResourceKey(key: String) = createResourceKey(splitResourceKey(key))

    fun <T : Any> getRegistryFromKey(key: ResourceKey<T>): Registry<T>? {
        val registry = BuiltInRegistries.REGISTRY.getOptional(key.registry()).getOrNull() ?: return null
        return registry as Registry<T>
    }

    fun <T : Any> getResource(key: ResourceKey<T>) = getRegistryFromKey(key)?.get(key)?.getOrNull()?.value()
    fun getResource(registry: Identifier, identifier: Identifier): Any? {
        return getResource(createResourceKey(registry, identifier) ?: return null)
    }
    fun getResource(identifiers: Pair<Identifier, Identifier>): Any? {
        return getResource(createResourceKey(identifiers) ?: return null)
    }
    fun getResource(key: String): Any? {
        return getResource(createResourceKey(key) ?: return null)
    }

    fun keyToString(key: ResourceKey<*>): String {
        return key.registry().toString() + " / " + key.identifier().toString()
    }

}