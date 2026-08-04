package net.jidb.to.base.service

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * A service that registers game objects into a [Registry], and creates new registries, the way the current modloader expects.
 * Neoforge defers registration until its own registry events fire, whereas Fabric writes into the registry immediately, so registration returns a supplier rather than the value itself.
 * Each loader project provides its own implementation, loaded through [Services].
 *
 * Usually accessed from the [Services] object:
 * ```kotlin
 * Services.register
 * ```
 *
 * This is the machinery behind [net.jidb.to.base.api.library.RegistryLibrary], which is what content should normally use.
 *
 * @since 0.0.3
 */
abstract class RegisterService {

    /**
     * Registers a value into the given registry under the given identifier, building the value lazily.
     *
     * @param T The type the registry holds.
     * @param E The type of the value being registered.
     * @param registry The registry to register the value into.
     * @param key The identifier to register the value under.
     * @param value A function that builds the value to register, given the identifier it is registered under.
     * @return A supplier of the registered value, which is only safe to call once registration has run.
     * @since 0.0.3
     */
    abstract fun <T : Any, E : T> register(registry: Registry<T>, key: Identifier, value: (key: Identifier) -> E): () -> E

    /**
     * Registers an already built value into the given registry under the given identifier.
     *
     * @param T The type the registry holds.
     * @param E The type of the value being registered.
     * @param registry The registry to register the value into.
     * @param key The identifier to register the value under.
     * @param value The value to register.
     * @return A supplier of the registered value, which is only safe to call once registration has run.
     * @since 0.0.3
     */
    fun <T : Any, E : T> register(registry: Registry<T>, key: Identifier, value: E): () -> E = register(registry, key) { value }

    /**
     * Registers a value into the given registry, so that the service itself can be called like a function.
     *
     * @param T The type the registry holds.
     * @param E The type of the value being registered.
     * @param registry The registry to register the value into.
     * @param key The identifier to register the value under.
     * @param value A function that builds the value to register, given the identifier it is registered under.
     * @return A supplier of the registered value, which is only safe to call once registration has run.
     * @since 0.0.3
     */
    operator fun <T : Any, E : T> invoke(registry: Registry<T>, key: Identifier, value: (key: Identifier) -> E) = register(registry, key, value)

    /**
     * Creates a new registry for the mod's own registry key, which each modloader has to do through its own registry builder.
     *
     * @param T The type the new registry will hold.
     * @param key The registry key to create the registry for.
     * @param default The identifier of the default entry of the registry, or `null` for a registry with no default. Defaults to `null`.
     * @param sync Whether the contents of the registry are synchronised from server to client. Defaults to `false`.
     * @return The newly created [Registry].
     * @since 0.0.3
     */
    abstract fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>, default: Identifier? = null, sync: Boolean = false): Registry<T>

}
