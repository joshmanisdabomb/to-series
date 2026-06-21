package net.jidb.to.base.api.library

import net.jidb.to.base.service.Services

abstract class SimpleRegistryLibrary<T : Any>(modid: String) : RegistryLibrary<T, T>(modid), SimpleLibraryBuilder<T> {

    override fun <U : T> i(entry: Library<T, T>.LibraryEntry<out T, out T>, input: () -> U): () -> U {
        return Services.register(registry, getEntryIdentifier(entry)) { input() }
    }

}