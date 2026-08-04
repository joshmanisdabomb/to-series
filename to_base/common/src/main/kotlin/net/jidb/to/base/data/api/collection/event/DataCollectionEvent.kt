package net.jidb.to.base.data.api.collection.event

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.module.DataCollectionModule

/**
 * One run of a data generator, which asks every collection for a particular kind of data and merges what comes back.
 * Merging happens in two passes, because both levels can conflict: several modules may generate for one entry, and several entries may generate into one file.
 *
 * @param M The type of what a single module generates.
 * @param C The type of what the modules of one collection merge into.
 * @param F The type of what every collection merges into, i.e. the finished result.
 * @since 0.3.0
 */
abstract class DataCollectionEvent<M : Any, C : Any, F : Any> {

    /**
     * What each module generated, indexed first by the collection it generated for and then by the module itself.
     *
     * @since 0.3.0
     */
    private val results = mutableMapOf<DataCollection<*>, MutableMap<DataCollectionModule, M>>()

    /**
     * Records what one module generated for one collection, which [DataCollectionModule] calls as it generates.
     *
     * @param collection The collection the result was generated for.
     * @param module The module that generated it.
     * @param result What was generated.
     * @since 0.3.0
     */
    open fun addResult(collection: DataCollection<*>, module: DataCollectionModule, result: M) {
        results.getOrPut(collection) { mutableMapOf() }[module] = result
    }

    /**
     * Merges everything the modules of a single collection generated.
     *
     * @param results What each module of the collection generated.
     * @return The merged result for that collection, or `null` where there is nothing to keep.
     * @since 0.3.0
     */
    abstract fun combineFromModules(results: Iterable<M>): C?

    /**
     * Merges what every collection generated into the finished result.
     *
     * @param results What each collection generated.
     * @return The finished result, or `null` where there is nothing to generate.
     * @since 0.3.0
     */
    abstract fun combineFromCollections(results: Iterable<C>): F?

    /**
     * Runs this event over the given collections and merges everything they generate.
     *
     * @param collections The collections to generate from.
     * @return The finished result, or `null` where nothing was generated.
     * @since 0.3.0
     */
    fun process(collections: Iterable<DataCollection<*>>): F? {
        collections.forEach { it.process(this) }
        val byCollection = results.map { combineFromModules(it.value.values) }.filterNotNull()
        return combineFromCollections(byCollection)
    }

}
