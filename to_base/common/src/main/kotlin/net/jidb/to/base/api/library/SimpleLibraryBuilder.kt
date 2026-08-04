package net.jidb.to.base.api.library

import net.jidb.to.base.api.library.Library.LibraryEntry

/**
 * This interface provides a default builder method for a [LibraryEntry] of a [SimpleLibrary] of the same input and final value type.
 * If the input does need to be transformed when determining the final value, the method can be overridden by the [Library].
 *
 * Extends the [AdvancedLibraryBuilder] interface.
 *
 * @param T The type of the input and final value provided by a [LibraryEntry].
 * @since 0.0.3
 */
interface SimpleLibraryBuilder<T> : AdvancedLibraryBuilder<T, T> {

    override fun <U : T> i(entry: Library<T, T>.LibraryEntry<out T, out T>, input: () -> U) = input

    /**
     * [LibraryEntry] delegate provider that uses the [SimpleLibraryBuilder] builder, so entries for this [Library] can be defined without `::i`:
     * ```kotlin
     * val custom_stone by this { Block(...) }
     * ```
     *
     * @param U The subtype of the input and final value.
     * @param initial The input supplier, i.e. the final value supplier in a simple builder.
     * @return A [LibraryEntry] built with and returning the same subtype [U].
     * @since 0.0.3
     */
    operator fun <U : T> invoke(initial: (Library<T, T>.LibraryEntry<U, U>) -> U): Library<T, T>.LibraryEntry<U, U> = invoke(::i, initial)

}
