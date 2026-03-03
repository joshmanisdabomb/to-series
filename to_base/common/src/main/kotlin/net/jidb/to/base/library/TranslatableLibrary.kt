package net.jidb.to.base.library

import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

interface TranslatableLibrary<I, V> {

    fun getEntryTranslationKey(entry: Library<I, V>.LibraryEntry<out I, out V>): String
    fun getEntryComponent(entry: Library<I, V>.LibraryEntry<out I, out V>) = Component.translatable(getEntryTranslationKey(entry))

}