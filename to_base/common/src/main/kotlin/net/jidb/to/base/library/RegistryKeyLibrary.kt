package net.jidb.to.base.library

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

interface RegistryKeyLibrary<I, V, R : Any> {

    val registry: Registry<R>

    fun getEntryIdentifier(entry: Library<I, V>.LibraryEntry<out I, out V>): Identifier
    fun getEntryResourceKey(entry: Library<I, V>.LibraryEntry<out I, out V>) = ResourceKey.create(registry.key(), getEntryIdentifier(entry))

}