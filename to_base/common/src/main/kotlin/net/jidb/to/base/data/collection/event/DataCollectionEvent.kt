package net.jidb.to.base.data.collection.event

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.module.DataCollectionModule

abstract class DataCollectionEvent<M : Any, C : Any, F : Any> {

    private val results = mutableMapOf<DataCollection<*>, MutableMap<DataCollectionModule, M>>()

    open fun addResult(collection: DataCollection<*>, module: DataCollectionModule, result: M) {
        results.getOrPut(collection) { mutableMapOf() }[module] = result
    }

    abstract fun combineFromModules(results: Iterable<M>): C?
    abstract fun combineFromCollections(results: Iterable<C>): F?

    fun process(collections: Iterable<DataCollection<*>>): F? {
        collections.forEach { it.process(this) }
        val byCollection = results.map { combineFromModules(it.value.values) }.filterNotNull()
        return combineFromCollections(byCollection)
    }

}
