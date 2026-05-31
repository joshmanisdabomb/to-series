package net.jidb.to.base.service

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

abstract class RegisterService {
    abstract fun <T : Any, E : T> register(registry: Registry<T>, key: Identifier, value: (key: Identifier) -> E): () -> E
    fun <T : Any, E : T> register(registry: Registry<T>, key: Identifier, value: E): () -> E = register(registry, key) { value }

    operator fun <T : Any, E : T> invoke(registry: Registry<T>, key: Identifier, value: (key: Identifier) -> E) = register(registry, key, value)

    abstract fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>, default: Identifier? = null, sync: Boolean = false): Registry<T>
}