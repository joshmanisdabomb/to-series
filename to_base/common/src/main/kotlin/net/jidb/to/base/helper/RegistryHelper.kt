package net.jidb.to.base.helper

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

object RegistryHelper {

    fun splitResourceKey(key: String): Pair<Identifier, Identifier> {
        val split = key.split(" / ")
        return Identifier.parse(split[0]) to Identifier.parse(split[1])
    }

    fun createResourceKey(registry: Identifier, identifier: Identifier): ResourceKey<*> {
        val registry = BuiltInRegistries.REGISTRY.getOptional(registry).orElseThrow()
        return registry.get(identifier).orElseThrow().key()
    }
    fun createResourceKey(identifiers: Pair<Identifier, Identifier>) = createResourceKey(identifiers.first, identifiers.second)
    fun createResourceKey(key: String) = createResourceKey(splitResourceKey(key))

    fun <T : Any> getRegistryFromKey(key: ResourceKey<T>): Registry<T> {
        return BuiltInRegistries.REGISTRY.getOptional(key.registry()).orElseThrow() as Registry<T>
    }

    fun <T : Any> getResource(key: ResourceKey<T>): T {
        return getRegistryFromKey(key).get(key).orElseThrow().value()
    }

    fun keyToString(key: ResourceKey<*>): String {
        return key.registry().toString() + " / " + key.identifier().toString()
    }

}