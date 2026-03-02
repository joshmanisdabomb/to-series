package net.jidb.to.base.library

abstract class AdvancedLibrary<I, V>(modid: String) : Library<I, V>(modid), AdvancedLibraryBuilder<I, V>