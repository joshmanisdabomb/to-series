package net.jidb.to.base.helper

import net.jidb.to.base.library.Library
import net.jidb.to.base.library.LibraryTagList
import net.jidb.to.base.library.RegistryKeyLibrary
import net.jidb.to.base.library.TranslatableLibrary

object LibraryHelper {
    fun <I, V, C : Library<I, V>, W : V> C.get(getter: C.() -> W): W = getter()

    fun <I, V, C : Library<I, V>, W : V> C.getEntries(getter: C.() -> W) = getEntries(get(getter))
    fun <I, V, C : Library<I, V>, W : V> C.getEntry(getter: C.() -> W) = getEntry(get(getter))

    fun <I, V, C : Library<I, V>, W : V> C.getIdentifier(getter: C.() -> W) = getEntryIdentifier(getEntry(getter)!!)

    fun <I, V, C : Library<I, V>, W : V, T> C.getTags(list: LibraryTagList<V, T>, getter: C.() -> W) = getEntryTags(list, getEntry(getter)!!)

    fun <I, V, C, W : V> C.getResourceKey(getter: C.() -> W) where C : Library<I, V>, C : RegistryKeyLibrary<I, V, *> = getEntryResourceKey(getEntry(getter)!!)

    fun <I, V, C, W : V> C.getTranslationKey(getter: C.() -> W) where C : Library<I, V>, C : TranslatableLibrary<I, V> = getEntryTranslationKey(getEntry(getter)!!)
    fun <I, V, C, W : V> C.getEntryComponent(getter: C.() -> W) where C : Library<I, V>, C : TranslatableLibrary<I, V> = getEntryComponent(getEntry(getter)!!)
}