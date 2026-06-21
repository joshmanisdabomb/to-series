package net.jidb.to.base.api.library

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

interface IResourceKeyLibrary<I, V, R : Any> {

    val registryKey: ResourceKey<out Registry<R>>

    fun getEntryIdentifier(entry: Library<I, V>.LibraryEntry<out I, out V>): Identifier
    fun getEntryResourceKey(entry: Library<I, V>.LibraryEntry<out I, out V>) = ResourceKey.create(registryKey, getEntryIdentifier(entry))

}