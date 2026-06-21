package net.jidb.to.base.data.api

import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import java.util.*

class AggregateTagLookup<T : Any>(val lookups: List<TagsProvider.TagLookup<T>>) : TagsProvider.TagLookup<T> {

    override fun apply(tag: TagKey<T>): Optional<TagBuilder> = Optional.ofNullable(lookups.map { it.apply(tag).orElse(null) }.firstOrNull())

}