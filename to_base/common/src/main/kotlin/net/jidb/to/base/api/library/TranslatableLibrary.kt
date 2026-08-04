package net.jidb.to.base.api.library

import net.jidb.to.base.api.library.Library.LibraryEntry
import net.minecraft.network.chat.Component

/**
 * [Library] interface that provides methods to handle [String] translation keys and [Component] objects for each [Library.LibraryEntry].
 *
 * @param I The type of the input to create a [LibraryEntry].
 * @param V The type of the final value provided by [LibraryEntry].
 * @since 0.0.4
 */
interface TranslatableLibrary<I, V> {

    /**
     * Retrieves the [String] translation key associated with a given [LibraryEntry].
     *
     * @param entry The [LibraryEntry] for which the translation key should be retrieved.
     * @return The translation key corresponding to the provided [LibraryEntry].
     * @since 0.0.4
     */
    fun getEntryTranslationKey(entry: Library<I, V>.LibraryEntry<out I, out V>): String

    /**
     * Retrieves a translatable [Component] for the provided [LibraryEntry].
     * Defaults to `Component.translatable` with [getEntryTranslationKey] as a translation key.
     *
     * @param entry The [LibraryEntry] for which a component is generated.
     * @return A [Component] created using the provided [LibraryEntry].
     * @since 0.0.4
     */
    fun getEntryComponent(entry: Library<I, V>.LibraryEntry<out I, out V>) = Component.translatable(getEntryTranslationKey(entry))

}
