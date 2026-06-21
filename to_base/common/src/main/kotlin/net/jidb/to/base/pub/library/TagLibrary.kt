package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.AdvancedLibrary
import net.jidb.to.base.api.library.AdvancedLibraryBuilder
import net.jidb.to.base.api.library.Library
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

abstract class TagLibrary<T : Any>(modid: String) : AdvancedLibrary<Identifier, TagKey<T>>(modid),
    AdvancedLibraryBuilder<Identifier, TagKey<T>> {

    abstract val registryKey: ResourceKey<Registry<T>>

    operator fun invoke() = invoke(::i) { getEntryIdentifier(it) }

    override fun <J : Identifier> i(entry: Library<Identifier, TagKey<T>>.LibraryEntry<out Identifier, out TagKey<T>>, input: () -> J) = { -> TagKey.create(registryKey, input()) }

}
