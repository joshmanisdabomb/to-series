package net.jidb.to.base.api.library

/**
 * [Library] implementation  with intentions to register content to a [net.minecraft.core.Registry].
 * Doesn't extend [AdvancedLibrary] or implement [AdvancedLibraryBuilder].
 *
 * @param I The type of the input used by the library.
 * @param V The type of the value stored in the registry. It must be a non-nullable type.
 * @param modid The mod ID associated with the library.
 * @since 0.0.3
 */
abstract class AdvancedRegistryLibrary<I, V : Any>(modid: String) : RegistryLibrary<I, V>(modid)
