package net.jidb.to.base.api.library

interface AdvancedLibraryBuilder<I, V> {

    fun <J : I> i(entry: Library<I, V>.LibraryEntry<out I, out V>, input: () -> J): () -> V

    operator fun <J : I, W : V> invoke(builder: Library<I, V>.LibraryEntry<out I, out V>.(() -> J) -> () -> W, initial: (Library<I, V>.LibraryEntry<J, W>) -> J): Library<I, V>.LibraryEntry<J, W> {
        val library = this as Library<I, V>
        return library.LibraryEntry(builder, initial)
    }

}
