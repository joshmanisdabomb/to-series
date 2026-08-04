package net.jidb.to.base.api.library

import net.jidb.to.base.service.Services

/**
 * [Library] implementation that registers content to a [net.minecraft.core.Registry] on [build] time, and provides access to that content in one place.
 * Doesn't extend [SimpleLibrary] but implements [SimpleLibraryBuilder].
 *
 * @param T The type of the elements to be registered in the library and the [net.minecraft.core.Registry].
 * @param modid The mod ID associated with the library.
 * @since 0.0.3
 */
abstract class SimpleRegistryLibrary<T : Any>(modid: String) : RegistryLibrary<T, T>(modid), SimpleLibraryBuilder<T> {

    override fun <U : T> i(entry: Library<T, T>.LibraryEntry<out T, out T>, input: () -> U) = Services.register(registry, getEntryIdentifier(entry)) { input() }

}
