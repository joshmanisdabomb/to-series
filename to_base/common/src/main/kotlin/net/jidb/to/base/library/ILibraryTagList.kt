package net.jidb.to.base.library

interface ILibraryTagList<T> {

    fun add(entry: Library<*, *>.LibraryEntry<*, *>, value: T)

    operator fun get(entry: Library<*, *>.LibraryEntry<*, *>): List<T>

    fun has(entry: Library<*, *>.LibraryEntry<*, *>, value: T) = get(entry).contains(value)

}
