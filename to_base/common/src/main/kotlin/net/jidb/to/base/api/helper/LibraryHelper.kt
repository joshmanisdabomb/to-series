package net.jidb.to.base.api.helper

import net.jidb.to.base.api.library.Library

object LibraryHelper {
    fun <I, V, C : Library<I, V>, W : V> C.get(getter: C.() -> W): W = getter()

    fun <I, V, C : Library<I, V>, W : V> C.getEntries(getter: C.() -> W) = getEntries(get(getter))
    fun <I, V, C : Library<I, V>, W : V> C.getEntry(getter: C.() -> W) = getEntry(get(getter))

    fun <I, V, C : Library<I, V>, W : V> C.getIdentifier(getter: C.() -> W) = getEntryIdentifier(getEntry(getter)!!)

    fun <I, V, C : Library<I, V>, W : V, T> C.getTags(list: net.jidb.to.base.api.library.LibraryTagList<V, T>, getter: C.() -> W) = getEntryTags(list, getEntry(getter)!!)

    fun <I, V, C, W : V, R> C.getResourceKey(getter: C.() -> W) where C : Library<I, V>, C : net.jidb.to.base.api.library.IResourceKeyLibrary<I, V, R> = getEntryResourceKey(getEntry(getter)!!)

    fun <I, V, C, W : V> C.getTranslationKey(getter: C.() -> W) where C : Library<I, V>, C : net.jidb.to.base.api.library.TranslatableLibrary<I, V> = getEntryTranslationKey(getEntry(getter)!!)
    fun <I, V, C, W : V> C.getEntryComponent(getter: C.() -> W) where C : Library<I, V>, C : net.jidb.to.base.api.library.TranslatableLibrary<I, V> = getEntryComponent(getEntry(getter)!!)
}