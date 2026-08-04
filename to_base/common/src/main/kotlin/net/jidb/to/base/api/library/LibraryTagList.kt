package net.jidb.to.base.api.library

/**
 * Implementation of [ILibraryTagList], to manage a list of tags associated with entries in a [Library].
 *
 * @param V The type of value that [Library.LibraryEntry] holds.
 * @param T The type of tag associated with these [Library] entries.
 * @since 0.0.3
 */
open class LibraryTagList<V, T> : ILibraryTagList<V, T> {

    /**
     * Internal mapping backing between [Library.LibraryEntry] instances and their associated tags.
     *
     * @since 0.1.0
     */
    private val _list: MutableMap<Library<*, V>.LibraryEntry<*, out V>, MutableList<T>> = mutableMapOf()

    /**
     * A map containing [Library.LibraryEntry] instances associated with their [List] of tags.
     *
     * @since 0.0.3
     */
    val list: Map<Library<*, V>.LibraryEntry<*, out V>, List<T>> = _list

    override fun add(entry: Library<*, V>.LibraryEntry<*, out V>, value: T) {
        _list.getOrPut(entry) { mutableListOf() }.add(value)
    }

    override operator fun get(entry: Library<*, V>.LibraryEntry<*, out V>) = list[entry] ?: emptyList()

}
