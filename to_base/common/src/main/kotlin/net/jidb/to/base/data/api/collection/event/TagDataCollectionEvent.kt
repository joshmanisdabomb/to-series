package net.jidb.to.base.data.api.collection.event

import net.minecraft.tags.TagKey

/**
 * The [DataCollectionEvent] generating tags, whose contents merge rather than replace at both levels.
 * A tag is a list, so two modules naming the same tag both contribute to it, unlike a loot table where the last one wins.
 *
 * @param T The type of content the generated tags hold.
 * @since 0.3.0
 */
abstract class TagDataCollectionEvent<T : Any> : DataCollectionEvent<Map<TagKey<T>, List<T>>, Map<TagKey<T>, List<T>>, Map<TagKey<T>, List<T>>>() {

    override fun combineFromModules(results: Iterable<Map<TagKey<T>, List<T>>>): Map<TagKey<T>, List<T>>? {
        if (results.count() <= 0) return null
        return results.flatMap { it.entries }.groupBy({ it.key }, { it.value }).mapValues { it.value.flatten() }
    }

    override fun combineFromCollections(results: Iterable<Map<TagKey<T>, List<T>>>) = combineFromModules(results)

}
