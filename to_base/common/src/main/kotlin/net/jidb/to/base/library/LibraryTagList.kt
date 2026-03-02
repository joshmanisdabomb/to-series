package net.jidb.to.base.library

class LibraryTagList<T> {

    private val list: MutableMap<String, MutableList<T>> = mutableMapOf()

    fun add(entry: Library<*, *>.LibraryEntry<*, *>, value: T) {
        list.getOrPut(entry.name) { mutableListOf() }.add(value)
    }

    operator fun get(entry: Library<*, *>.LibraryEntry<*, *>) = list[entry.name] ?: emptyList()
    fun has(entry: Library<*, *>.LibraryEntry<*, *>, value: T) = get(entry).contains(value)

}
