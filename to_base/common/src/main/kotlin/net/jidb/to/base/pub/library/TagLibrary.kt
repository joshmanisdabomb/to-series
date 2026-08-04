package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.AdvancedLibrary
import net.jidb.to.base.api.library.AdvancedLibraryBuilder
import net.jidb.to.base.api.library.Library
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey

/**
 * [AdvancedLibrary] implementation that declares a [TagKey] for each entry, and provides access to those tags in one place.
 * Nothing is registered: a tag key is only a name, and the contents behind it come from the data pack, so the library exists to give each tag a single Kotlin property to be referred to by.
 *
 * @param T The type of the content the declared tags hold, such as [net.minecraft.world.level.block.Block].
 * @param modid The mod ID associated with the library.
 * @since 0.1.0
 */
abstract class TagLibrary<T : Any>(modid: String) : AdvancedLibrary<Identifier, TagKey<T>>(modid),
    AdvancedLibraryBuilder<Identifier, TagKey<T>> {

    /**
     * The key of the [Registry] the declared tags apply to, which subclasses supply for the content type they cover.
     *
     * @since 0.1.0
     */
    abstract val registryKey: ResourceKey<Registry<T>>

    /**
     * Declares a tag named after the property it is assigned to, in the namespace of this library.
     *
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.1.0
     */
    operator fun invoke() = invoke(::i) { getEntryIdentifier(it) }

    override fun <J : Identifier> i(entry: Library<Identifier, TagKey<T>>.LibraryEntry<out Identifier, out TagKey<T>>, input: () -> J) = { -> TagKey.create(registryKey, input()) }

}
