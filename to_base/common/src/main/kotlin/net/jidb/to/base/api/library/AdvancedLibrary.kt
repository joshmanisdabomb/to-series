package net.jidb.to.base.api.library

/**
 * An abstract extension of the [Library] class, signaling an intention to have the input value type be different from the final value type.
 * Doesn't implement [AdvancedLibraryBuilder] by default.
 *
 * @param I The type of the input to create a [LibraryEntry].
 * @param V The type of the final value provided by [LibraryEntry].
 * @param modid The mod ID for this library.
 * @see AdvancedLibraryBuilder
 * @since 0.0.3
 */
abstract class AdvancedLibrary<I, V>(modid: String) : Library<I, V>(modid)
