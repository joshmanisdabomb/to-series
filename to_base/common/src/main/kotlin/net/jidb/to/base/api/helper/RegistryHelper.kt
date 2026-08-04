package net.jidb.to.base.api.helper

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import kotlin.jvm.optionals.getOrNull

/**
 * Utility object for interacting with built-in registries and resource keys in a structured way.
 *
 * @since 0.1.0
 */
object RegistryHelper {

    /**
     * Retrieves the resource key associated with the specified [Block].
     *
     * @param block The [Block] to retrieve the resource key for.
     * @return The resource key for the given [Block].
     * @since 0.8.0
     */
    operator fun get(block: Block) = BuiltInRegistries.BLOCK.getResourceKey(block).get()

    /**
     * Retrieves the resource key associated with the specified [Block].
     *
     * @return The resource key for the given [Block].
     * @since 0.8.0
     */
    val Block.resourceKey get() = get(this)

    /**
     * Retrieves the resource key associated with the specified [Item].
     *
     * @param item The [Item] to retrieve the resource key for.
     * @return The resource key for the given [Item].
     * @since 0.8.0
     */
    operator fun get(item: Item) = BuiltInRegistries.ITEM.getResourceKey(item).get()

    /**
     * Retrieves the resource key associated with the specified [Item].
     *
     * @return The resource key for the given [Item].
     * @since 0.8.0
     */
    val Item.resourceKey get() = get(this)

    /**
     * Splits a resource key into two separate identifiers.
     *
     * @param key The resource key in the format "namespace:path / namespace:path".
     * @return A pair of identifiers.
     * @since 0.1.0
     */
    fun splitResourceKey(key: String): Pair<Identifier, Identifier> {
        val split = key.split(" / ")
        return Identifier.parse(split[0]) to Identifier.parse(split[1])
    }

    /**
     * Creates a resource key for the given registry and identifier.
     *
     * @param registry The identifier for the registry.
     * @param identifier The identifier of the resource.
     * @return The resource key associated with the given registry and identifier, or null if not found.
     * @since 0.1.0
     */
    fun createResourceKey(registry: Identifier, identifier: Identifier): ResourceKey<*>? {
        val registry = BuiltInRegistries.REGISTRY.getOptional(registry).getOrNull() ?: return null
        return registry.get(identifier).getOrNull()?.key()
    }

    /**
     * Creates a resource key for the given registry and identifier.
     *
     * @param identifiers A pair of identifiers, the first being the registry and the second being the resource identifier.
     * @return The resource key associated with the given registry and identifier, or null if not found.
     * @since 0.1.0
     */
    fun createResourceKey(identifiers: Pair<Identifier, Identifier>) = createResourceKey(identifiers.first, identifiers.second)

    /**
     * Creates a resource key for the given registry and identifier.
     *
     * @param key A resource key [String] that will be split via [splitResourceKey].
     * @return The resource key associated with the given registry and identifier, or null if not found.
     * @since 0.1.0
     */
    fun createResourceKey(key: String) = createResourceKey(splitResourceKey(key))

    /**
     * Retrieves a registry corresponding to the provided resource key.
     *
     * @param T The type of elements stored in the registry.
     * @param key The resource key identifying the registry.
     * @return The registry associated with the given key, or `null` if no such registry exists.
     * @since 0.1.0
     */
    fun <T : Any> getRegistryFromKey(key: ResourceKey<T>): Registry<T>? {
        val registry = BuiltInRegistries.REGISTRY.getOptional(key.registry()).getOrNull() ?: return null
        return registry as Registry<T>
    }

    /**
     * Retrieves a resource associated with the specified resource key.
     *
     * @param T The type of the resource.
     * @param key The resource key used to identify and fetch the resource.
     * @return The resource associated with the given key, or `null` if the resource cannot be found.
     * @since 0.1.0
     */
    fun <T : Any> getResource(key: ResourceKey<T>) = getRegistryFromKey(key)?.get(key)?.getOrNull()?.value()

    /**
     * Retrieves a resource associated with the specified registry and identifier.
     *
     * @param registry The identifier for the registry where the resource is registered.
     * @param identifier The identifier of the resource within the specified registry.
     * @return The resource associated with the given registry and identifier, or `null` if the resource cannot be found.
     * @since 0.1.0
     */
    fun getResource(registry: Identifier, identifier: Identifier): Any? {
        return getResource(createResourceKey(registry, identifier) ?: return null)
    }

    /**
     * Retrieves a resource associated with the specified registry and identifier.
     *
     * @param identifiers A pair of identifiers, the first being the registry and the second being the resource identifier.
     * @return The resource associated with the given identifiers, or `null` if the resource cannot be found.
     * @since 0.1.0
     */
    fun getResource(identifiers: Pair<Identifier, Identifier>): Any? {
        return getResource(createResourceKey(identifiers) ?: return null)
    }

    /**
     * Retrieves a resource associated with the specified registry and identifier.
     *
     * @param key A resource key [String] that will be converted to a [ResourceKey] via [createResourceKey].
     * @return The resource associated with the given resource key, or `null` if the resource cannot be found.
     * @since 0.1.0
     */
    fun getResource(key: String): Any? {
        return getResource(createResourceKey(key) ?: return null)
    }

    /**
     * Converts a [ResourceKey] into a string representation by combining its registry and identifier.
     *
     * @param key The [ResourceKey] instance to be converted into a [String] representation.
     * @return The [String] representation of the [ResourceKey] in the form "namespace:registry / namespace:identifier".
     * @since 0.1.0
     */
    fun keyToString(key: ResourceKey<*>) = key.registry().toString() + " / " + key.identifier().toString()

}
