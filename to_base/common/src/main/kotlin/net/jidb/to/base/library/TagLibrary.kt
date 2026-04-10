package net.jidb.to.base.library

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

abstract class TagLibrary<T : Any>(modid: String) : AdvancedLibrary<Identifier, TagKey<T>>(modid) {

    abstract val registryKey: ResourceKey<Registry<T>>

    operator fun invoke() = invoke(::i) { getEntryIdentifier(it) }

    override fun <J : Identifier> i(entry: Library<Identifier, TagKey<T>>.LibraryEntry<out Identifier, out TagKey<T>>, input: () -> J) = { -> TagKey.create(registryKey, input()) }

}
