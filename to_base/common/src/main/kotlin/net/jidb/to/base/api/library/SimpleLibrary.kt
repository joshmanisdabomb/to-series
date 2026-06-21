package net.jidb.to.base.api.library

open class SimpleLibrary<T>(modid: String) : Library<T, T>(modid), SimpleLibraryBuilder<T>