package net.jidb.to.base.api.library

import net.jidb.to.base.service.Services
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * A [Library] implementation for registering [Registry] objects.
 * Extends the functionality of the [SimpleLibrary] class to work specifically with [Registry] instances.
 *
 * @param modid The identifier for the mod associated with this library.
 * @since 0.5.0
 */
abstract class RegistryRegistryLibrary(modid: String) : SimpleLibrary<Registry<*>>(modid) {

    /**
     * [LibraryEntry] delegate provider with parameters to quickly build a [Registry].
     *
     * @param T The type of values in the [Registry] to be registered.
     * @param key The [ResourceKey] for the [Registry] to be registered under. Defaults to the [LibraryEntry.id] i.e. property name.
     * @param default The [Identifier] for the default value within the [Registry].
     * @param sync Whether this [Registry] should be synced over the network automatically.
     * @return A [LibraryEntry] for the specified [Registry] type.
     * @since 0.5.0
     */
    operator fun <T : Any> invoke(key: ResourceKey<Registry<T>>? = null, default: Identifier? = null, sync: Boolean = false): Library<Registry<*>, Registry<*>>.LibraryEntry<Registry<T>, Registry<T>> {
        return this.LibraryEntry({
            val registry = it()
            return@LibraryEntry { registry }
        }, { Services.register.createRegistry(key ?: ResourceKey.createRegistryKey(it.id), default, sync) })
    }

}
