package net.jidb.to.base.data.api

import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import java.util.Optional

/**
 * A [TagsProvider.TagLookup] that consults several others in turn, answering with the first that knows the tag.
 * Vanilla gives a tag provider one lookup for the tags generated before it, which is not enough where the tags of a run are split across several providers, as they are here.
 *
 * @param T The type of content the tags hold.
 * @property lookups The lookups to consult, in the order they are consulted.
 * @since 0.3.0
 */
class AggregateTagLookup<T : Any>(val lookups: List<TagsProvider.TagLookup<T>>) : TagsProvider.TagLookup<T> {

    override fun apply(tag: TagKey<T>): Optional<TagBuilder> = Optional.ofNullable(lookups.map { it.apply(tag).orElse(null) }.firstOrNull())

}
