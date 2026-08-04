package net.jidb.to.base.api.library

/**
 * An abstract extension of the [Library] class, which sets the input value and the final value type as the same.
 * Implements [SimpleLibraryBuilder] by default.
 *
 * @param T The type of the input and final value provided by a [LibraryEntry].
 * @param modid The mod ID for this library.
 * @see SimpleLibraryBuilder
 * @since 0.0.3
 */
open class SimpleLibrary<T>(modid: String) : Library<T, T>(modid), SimpleLibraryBuilder<T>
