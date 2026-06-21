package net.jidb.to.base.api.library

import net.minecraft.network.chat.Component

interface TranslatableLibrary<I, V> {

    fun getEntryTranslationKey(entry: Library<I, V>.LibraryEntry<out I, out V>): String
    fun getEntryComponent(entry: Library<I, V>.LibraryEntry<out I, out V>) = Component.translatable(getEntryTranslationKey(entry))

}