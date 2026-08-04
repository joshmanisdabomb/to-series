package net.jidb.to.base.data.api.collection

import net.jidb.to.base.api.helper.RegistryHelper
import net.jidb.to.base.data.api.collection.event.DataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.jidb.to.base.data.api.collection.module.IDataCollectionModule
import net.minecraft.resources.ResourceKey

/**
 * Everything that is generated for one registry entry, i.e. the modules a [DataCollectionDescription] resolved to for that entry.
 * A collection is built per entry when a [net.jidb.to.base.data.api.library.DataCollectionLibrary] is built, and is asked for its data once per generator that runs.
 *
 * @param T The type of the registry entry this collection generates data for.
 * @property entry The registry entry this collection generates data for.
 * @since 0.3.0
 */
open class DataCollection<T : Any>(val entry: ResourceKey<T>) {

    /**
     * The registry object [entry] points at, resolved on first use so that the collection can be built before the registry has been filled.
     *
     * @since 0.3.0
     */
    val `object` by lazy { RegistryHelper.getResource(entry)!! }

    /**
     * The modules that always run for this entry.
     *
     * @since 0.3.0
     */
    protected val modules = mutableListOf<DataCollectionModule>()

    /**
     * The modules that run for this entry only where no module in [modules] has answered.
     *
     * @since 0.3.0
     */
    protected val defaults = mutableListOf<DataCollectionModule>()

    /**
     * Resolves each of the given descriptions against this collection and takes on the modules they build.
     * A factory that returns `null` is skipped rather than added, which is how a module declines an entry it has nothing to say about.
     *
     * @param description The descriptions to take modules from.
     * @since 0.3.0
     */
    fun add(vararg description: DataCollectionDescription) {
        description.forEach {
            it.modules.forEach { addModule(it(this) ?: return@forEach) }
            it.defaults.forEach { addDefaultModule(it(this) ?: return@forEach) }
        }
    }

    /**
     * Adds a module that always runs for this entry.
     *
     * @param module The module to add.
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun addModule(module: DataCollectionModule): DataCollection<T> {
        modules.add(module)
        return this
    }

    /**
     * Adds a module that runs for this entry only where no ordinary module has answered.
     *
     * @param module The module to add.
     * @return The current instance for further chaining.
     * @since 0.3.0
     */
    fun addDefaultModule(module: DataCollectionModule): DataCollection<T> {
        defaults.add(module)
        return this
    }

    /**
     * Offers this collection's entry to every module for the given event, gathering whatever each of them generates onto the event itself.
     *
     * The ordinary modules run first. The default modules run afterwards only where every one of them passed, so declaring a module for a kind of data replaces the default for that kind alone.
     * A module answering with [IDataCollectionModule.EventResult.SUCCESS_BLOCK] stops the rest from being asked at all.
     *
     * @param event The event being generated for.
     * @since 0.3.0
     */
    open fun process(event: DataCollectionEvent<*, *, *>) {
        var fallback = true
        for (it in modules) {
            val result = it.process(this, event)
            if (result != IDataCollectionModule.EventResult.PASS) fallback = false
            if (result == IDataCollectionModule.EventResult.SUCCESS_BLOCK) return
        }
        if (!fallback) return
        for (it in defaults) {
            val result = it.process(this, event)
            if (result == IDataCollectionModule.EventResult.SUCCESS_BLOCK) return
        }
    }

}
