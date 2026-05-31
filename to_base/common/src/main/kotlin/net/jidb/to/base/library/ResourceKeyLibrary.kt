package net.jidb.to.base.library

import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

abstract class ResourceKeyLibrary<T : Any>(modid: String) : AdvancedLibrary<Identifier, ResourceKey<T>>(modid), IResourceKeyLibrary<Identifier, ResourceKey<T>, T>, AdvancedLibraryBuilder<Identifier, ResourceKey<T>> {

    override fun <J : Identifier> i(entry: Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>>, input: () -> J): () -> ResourceKey<T> {
        return { ResourceKey.create(registryKey, input()) }
    }

    operator fun invoke(): Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>> = invoke(::i) { it.id }
    operator fun invoke(id: Identifier): Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>> = invoke(::i) { id }

    override fun getEntryIdentifier(entry: Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>>) = entry.value.identifier()
    override fun getEntryResourceKey(entry: Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>>) = entry.value

}
