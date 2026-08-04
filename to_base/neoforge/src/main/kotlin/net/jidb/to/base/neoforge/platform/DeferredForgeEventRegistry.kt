package net.jidb.to.base.neoforge.platform

import net.neoforged.bus.api.Event
import net.neoforged.bus.api.IEventBus

/**
 * Holds callbacks against one of Neoforge's events until that event is actually raised.
 *
 * Callbacks are kept per mod, because each mod has its own bus and only sees the events raised on it.
 *
 * @param E The type of the Neoforge event being waited on.
 * @property event The class of that event, which is what the listener is bound to.
 * @property listenerFactory Builds the object that runs the queued callbacks, for an event that needs something doing either side of them. Defaults to running them one after another.
 * @since 0.2.0
 */
open class DeferredForgeEventRegistry<E : Event>(val event: Class<E>, val listenerFactory: (modid: String, event: E) -> CollectedEventListener<E> = ::CollectedEventListener) {

    /**
     * The callbacks waiting on the event, keyed by the mod that declared them.
     *
     * @since 0.2.0
     */
    private val callbacks = mutableMapOf<String, MutableList<(event: E) -> Unit>>()

    /**
     * Queues a callback to run the next time the event is raised on a mod's bus.
     *
     * @param modid The mod ID the callback belongs to.
     * @param callback The callback to queue.
     * @since 0.2.0
     */
    internal fun register(modid: String, callback: (event: E) -> Unit) {
        callbacks.getOrPut(modid) { mutableListOf() }.add(callback)
    }

    /**
     * Runs everything a mod queued, in the order it was declared.
     *
     * @param modid The mod ID whose callbacks are being run.
     * @param event The event that was raised.
     * @since 0.2.0
     */
    fun handle(modid: String, event: E) {
        val callbacks = callbacks[modid] ?: return
        val listener = listenerFactory(modid, event)
        listener.start()
        listener.run(callbacks)
        listener.end()
    }

    /**
     * Binds this registry to a mod's bus, so that the callbacks it queued run when the event is raised there.
     *
     * @param modid The mod ID whose callbacks the bus belongs to.
     * @param bus The bus to listen on.
     * @since 0.2.0
     */
    fun addListener(modid: String, bus: IEventBus) {
        bus.addListener(event, { handle(modid, it) })
    }

    /**
     * Runs the callbacks queued for one raising of the event, with a place to do something before and after them.
     *
     * @param E The type of the Neoforge event being handled.
     * @property modid The mod ID whose callbacks are being run.
     * @property event The event that was raised.
     * @since 0.2.0
     */
    open class CollectedEventListener<E : Event>(protected val modid: String, protected val event: E) {

        /**
         * Called before any of the callbacks are run.
         *
         * @return [Unit]
         * @since 0.2.0
         */
        open fun start() = Unit

        /**
         * Runs the queued callbacks.
         *
         * @param callbacks The callbacks to run, in the order they were declared.
         * @since 0.2.0
         */
        open fun run(callbacks: List<(event: E) -> Unit>) {
            callbacks.forEach { it(event) }
        }

        /**
         * Called after all of the callbacks have run.
         *
         * @return [Unit]
         * @since 0.2.0
         */
        open fun end() = Unit

    }

}
