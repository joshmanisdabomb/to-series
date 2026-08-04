package net.jidb.to.base.api.library

/**
 * A contract for managing a list of tags associated with elements in a [Library].
 *
 * @param V The type of value that [Library.LibraryEntry] holds.
 * @param T The type of tag associated with these [Library] entries.
 * @since 0.1.0
 */
interface ILibraryTagList<V, T> {

    /**
     * Associates the given tag value with the specified [Library.LibraryEntry].
     *
     * @param entry The [Library.LibraryEntry] to associate the tag with.
     * @param value The tag to be associated with the given [Library.LibraryEntry].
     * @since 0.1.0
     */
    fun add(entry: Library<*, V>.LibraryEntry<*, out V>, value: T)

    /**
     * Retrieves the [List] of tags associated with the specified [Library.LibraryEntry].
     *
     * @param entry The [Library.LibraryEntry] for which to retrieve the associated tags.
     * @return A list of tags associated with the given [Library.LibraryEntry].
     * @since 0.1.0
     */
    operator fun get(entry: Library<*, V>.LibraryEntry<*, out V>): List<T>

    /**
     * Checks if the specified tag value is associated with the given [Library.LibraryEntry].
     *
     * @param entry The [Library.LibraryEntry] to check for the presence of the tag.
     * @param value The tag to verify association with the given [Library.LibraryEntry].
     * @since 0.1.0
     * @return `true` if the tag value is associated with the specified entry, otherwise `false`.
     */
    fun has(entry: Library<*, V>.LibraryEntry<*, out V>, value: T) = get(entry).contains(value)

}
