package net.jidb.to.base.data.collection.event

import net.minecraft.tags.TagKey

abstract class TagDataCollectionEvent<T : Any>() : DataCollectionEvent<Map<TagKey<T>, List<T>>, Map<TagKey<T>, List<T>>, Map<TagKey<T>, List<T>>>() {

    override fun combineFromModules(results: Iterable<Map<TagKey<T>, List<T>>>): Map<TagKey<T>, List<T>>? {
        if (results.count() <= 0) return null
        return results.flatMap { it.entries }.groupBy({ it.key }, { it.value }).mapValues { it.value.flatten() }
    }

    override fun combineFromCollections(results: Iterable<Map<TagKey<T>, List<T>>>) = combineFromModules(results)

}
