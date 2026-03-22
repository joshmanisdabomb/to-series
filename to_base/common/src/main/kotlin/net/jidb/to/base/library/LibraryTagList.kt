package net.jidb.to.base.library

open class LibraryTagList<T> : ILibraryTagList<T> {

    private val _list: MutableMap<Library<*, *>.LibraryEntry<*, *>, MutableList<T>> = mutableMapOf()
    val list: Map<Library<*, *>.LibraryEntry<*, *>, MutableList<T>> = _list

    override fun add(entry: Library<*, *>.LibraryEntry<*, *>, value: T) {
        _list.getOrPut(entry) { mutableListOf() }.add(value)
    }

    override operator fun get(entry: Library<*, *>.LibraryEntry<*, *>) = list[entry] ?: emptyList()

}
