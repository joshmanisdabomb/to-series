package net.jidb.to.base.library

interface AdvancedLibraryBuilder<I, V> {

    fun <J : I> i(entry: Library<I, V>.LibraryEntry<out I, out V>, input: () -> J): () -> V

}
