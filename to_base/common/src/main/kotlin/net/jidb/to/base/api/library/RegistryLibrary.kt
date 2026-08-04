package net.jidb.to.base.api.library

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

/**
 * Abstract [Library] class with intentions to register content to a [net.minecraft.core.Registry].
 *
 * This class extends the `Library` class and implements the `IResourceKeyLibrary` interface.
 * It provides functionality to work with a specific registry and its associated resource keys.
 *
 * @param I The type of the input used by the library.
 * @param V The type of the value stored in the registry. It must be a non-nullable type.
 * @param modid The mod ID associated with the library.
 * @since 0.0.3
 */
sealed class RegistryLibrary<I, V : Any>(modid: String) : Library<I, V>(modid), IResourceKeyLibrary<I, V, V> {

    /**
     * The [Registry] instance that the [RegistryLibrary] registers its values to.
     *
     * @since 0.0.3
     */
    abstract val registry: Registry<V>
    override val registryKey: ResourceKey<out Registry<V>> get() = registry.key()

}
