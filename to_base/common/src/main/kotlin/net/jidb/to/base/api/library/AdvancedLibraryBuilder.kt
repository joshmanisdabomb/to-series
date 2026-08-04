package net.jidb.to.base.api.library

import net.jidb.to.base.api.library.Library.LibraryEntry

/**
 * This interface provides an abstract builder method for a [LibraryEntry] of an [AdvancedLibrary], defining how an input supplier is transformed into the final value supplier.
 *
 * @param I The type of the input to create a [LibraryEntry].
 * @param V The type of the final value provided by [LibraryEntry].
 * @since 0.0.3
 */
interface AdvancedLibraryBuilder<I, V> {

    /**
     * The builder function to be passed to [invoke]. It defines how the input supplier is transformed to the final value supplier in a [LibraryEntry].
     * A builder is required so subtypes of [I] and [V] can be properly inferred for the delegate.
     *
     * @param J The subtype of the input value.
     * @param entry The [LibraryEntry] being built.
     * @param input The input supplier.
     * @return The final value supplier. The subtype of the final value is lost.
     * @since 0.0.3
     */
    fun <J : I> i(entry: Library<I, V>.LibraryEntry<out I, out V>, input: () -> J): () -> V

    /**
     * Base [LibraryEntry] delegate provider that takes the [AdvancedLibraryBuilder] builder [i] (or any compatible builder):
     * ```kotlin
     * class ExampleLibrary : AdvancedLibrary<Block, Item>("modid") {
     *     val custom: Item by this(::i) { Block(...) }
     *
     *     fun i(entry: Library.LibraryEntry, input: () -> Block) = { input().asItem() }
     * }
     * ```
     *
     * @param J The subtype of the input value.
     * @param W The subtype of the final value.
     * @param builder The builder that transforms the input supplier into the final value supplier.
     * @param initial The input supplier.
     * @return The built [LibraryEntry] with an input supplier and final value supplier.
     *
     * @since 0.0.3
     */
    operator fun <J : I, W : V> invoke(builder: Library<I, V>.LibraryEntry<out I, out V>.(() -> J) -> () -> W, initial: (Library<I, V>.LibraryEntry<J, W>) -> J): Library<I, V>.LibraryEntry<J, W> {
        val library = this as Library<I, V>
        return library.LibraryEntry(builder, initial)
    }

}
