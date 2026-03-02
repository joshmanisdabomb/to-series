package net.jidb.to.base.library

interface SimpleLibraryBuilder<T> : AdvancedLibraryBuilder<T, T> {

    override fun <U : T> i(entry: Library<T, T>.LibraryEntry<out T, out T>, input: () -> U) = input

    operator fun <U : T> invoke(initial: (Library<T, T>.LibraryEntry<U, U>) -> U): Library<T, T>.LibraryEntry<U, U> {
        val library = this as Library<T, T>
        return library.LibraryEntry(::i, initial)
    }

}
