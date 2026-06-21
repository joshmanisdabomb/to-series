package net.jidb.to.base.api.library

open class LibraryTagList<V, T> : ILibraryTagList<V, T> {

    private val _list: MutableMap<Library<*, V>.LibraryEntry<*, out V>, MutableList<T>> = mutableMapOf()
    val list: Map<Library<*, V>.LibraryEntry<*, out V>, MutableList<T>> = _list

    override fun add(entry: Library<*, V>.LibraryEntry<*, out V>, value: T) {
        _list.getOrPut(entry) { mutableListOf() }.add(value)
    }

    override operator fun get(entry: Library<*, V>.LibraryEntry<*, out V>) = list[entry] ?: emptyList()

}
