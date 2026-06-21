package net.jidb.to.base.api.library

interface ILibraryTagList<V, T> {

    fun add(entry: Library<*, V>.LibraryEntry<*, out V>, value: T)

    operator fun get(entry: Library<*, V>.LibraryEntry<*, out V>): List<T>

    fun has(entry: Library<*, V>.LibraryEntry<*, out V>, value: T) = get(entry).contains(value)

}
