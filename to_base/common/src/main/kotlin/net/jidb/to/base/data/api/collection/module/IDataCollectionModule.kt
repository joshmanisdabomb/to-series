package net.jidb.to.base.data.api.collection.module

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.DataCollectionEvent

/**
 * The contract a data generation module answers to: given a collection and an event, generate something or pass.
 * [DataCollectionModule] implements this by dispatching on the kind of event, so a module normally extends that rather than implementing this directly.
 *
 * @since 0.3.0
 */
interface IDataCollectionModule {

    /**
     * Generates whatever this module has to contribute for the given collection and event.
     *
     * @param collection The collection being generated for.
     * @param event The event being generated for.
     * @return How the module answered, which decides whether the default modules still run.
     * @since 0.3.0
     */
    fun process(collection: DataCollection<*>, event: DataCollectionEvent<*, *, *>): EventResult

    /**
     * Enum that defines how a module answered a call to [process].
     *
     * @since 0.3.0
     */
    enum class EventResult {

        /**
         * The module generated something, and the remaining modules are still asked.
         *
         * @since 0.3.0
         */
        SUCCESS,

        /**
         * The module had nothing to generate, so the default modules may still answer in its place.
         *
         * @since 0.3.0
         */
        PASS,

        /**
         * The module generated something, and the remaining modules are not asked at all.
         *
         * @since 0.3.0
         */
        SUCCESS_BLOCK

    }

}
