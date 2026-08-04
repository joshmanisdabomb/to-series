package net.jidb.to.base.api.library

import net.jidb.to.base.api.library.Library.LibraryEntry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * Represents a [Library] interface for [ResourceKey] managing associated with a specific [Registry] type.
 * It provides mechanisms to retrieve identifiers and resource keys for library entries.
 *
 * @param I The type of the input to create a [LibraryEntry].
 * @param V The type of the final value provided by [LibraryEntry].
 * @param R The type of the registry associated with the resource keys. Could be different from [V].
 * @since 0.2.0
 */
interface IResourceKeyLibrary<I, V, R : Any> {

    /**
     * The resource key associated with a specific [Registry] type (of [R]).
     * This key is used to link entries in the [Library] to a [Registry].
     *
     * @since 0.2.0
     */
    val registryKey: ResourceKey<out Registry<R>>

    /**
     * Retrieves the [Identifier] associated with a given [LibraryEntry].
     * Can be overridden by subclasses to provide different implementations of retrieving an identifier from an entry.
     *
     * @param entry The [LibraryEntry] whose identifier is to be retrieved.
     * @return The [Identifier] of the specified [LibraryEntry].
     * @see LibraryEntry.id
     * @since 0.2.0
     */
    fun getEntryIdentifier(entry: Library<I, V>.LibraryEntry<out I, out V>): Identifier

    /**
     * Retrieves the [ResourceKey] associated with a given [LibraryEntry].
     * Can be overridden by subclasses to provide different implementations of retrieving a resource key from an entry.
     *
     * @param entry The [LibraryEntry] whose key is to be retrieved.
     * @return The [ResourceKey] of the specified [LibraryEntry].
     * @since 0.2.0
     */
    fun getEntryResourceKey(entry: Library<I, V>.LibraryEntry<out I, out V>) = ResourceKey.create(registryKey, getEntryIdentifier(entry))

}
