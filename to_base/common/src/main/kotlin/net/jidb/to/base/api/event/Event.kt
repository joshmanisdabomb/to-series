package net.jidb.to.base.api.event

import net.minecraft.resources.Identifier

/**
 * Represents a single cross-platform event that [EventHandler] callbacks can attach to, to be processed when this event fires.
 * Uses a similar but separate system to Fabric events and Forge events.
 *
 * @param C The type of the event context, the input data for the event.
 * @param R The type of the result produced by event handlers, may implement [EventResult] for cancelling.
 * @property id A unique [Identifier] for the event.
 * @since 0.5.0
 */
abstract class Event<C, R>(val id: Identifier) {

    /**
     * Attaches a callback to this event. The handler is wrapped in a [SimpleEventHandler] and will be executed when the event is triggered.
     *
     * @param handler A function that takes a context of type `C` and returns a result of type `R`. The handler processes the event context and produces a result.
     * @return The current event instance for further chaining.
     * @since 0.5.0
     */
    fun attach(handler: (context: C) -> R): Event<C, R> {
        attach(id, handler)
        return this
    }

    /**
     * Attaches an [EventHandler] to this event, which will be executed when the event is triggered.
     *
     * @param handler An event handler object that takes a context of type `C` and returns a result of type `R`. The handler processes the event context and produces a result.
     * @return The current event instance for further chaining.
     * @since 0.5.0
     */
    fun attach(handler: EventHandler<C, R>): Event<C, R> {
        attach(id, handler)
        return this
    }

    /**
     * Fires the event, executing all attached handlers to this event with the provided context and collects their results.
     *
     * @param context The context of type `C` that is passed to all attached event handlers for processing.
     * @return [EventResults] containing the results from all run event handlers. [EventResults.cancelled] indicates the last event handler cancelled any after from running.
     * @since 0.5.0
     */
    fun call(context: C): EventResults<R> {
        val results = mutableListOf<R>()
        val handlers = handlers[id] ?: return EventResults()
        for (handler in handlers) {
            val result = (handler as EventHandler<C, R>)(context)
            results.add(result)
            if ((result as? EventResult)?.cancelAfter() == true) {
                return EventResults(results, true)
            }
        }
        return EventResults(results, false)
    }

    companion object {

        /**
         * A map that associates event identifiers ([id]) with their associated event handlers to run.
         *
         * @since 0.5.0
         */
        private val handlers = mutableMapOf<Identifier, MutableList<EventHandler<*, *>>>()

        /**
         * Attaches a handler function to the given event. The handler is wrapped in a [SimpleEventHandler] and will be executed when the event is triggered.
         *
         * @param C The type of the context, the input data for the event.
         * @param R The type of the result that the event requires, returned by the handler function.
         * @param event The [Identifier] of the event to which the handler will be attached.
         * @param handler A function that processes the event context and returns a result.
         * @return [Unit]
         * @since 0.5.0
         */
        fun <C, R> attach(event: Identifier, handler: (context: C) -> R) = attach(event, SimpleEventHandler(handler))

        /**
         * Attaches an [EventHandler] to the specified event, which will be executed when the event is triggered.
         *
         * @param C The type of the context, the input data for the event.
         * @param R The type of the result that the event requires, returned by the handler.
         * @param event The [Identifier] of the event to which the handler will be attached.
         * @param handler The event handler that processes the event context and returns a result.
         * @since 0.5.0
         */
        fun <C, R> attach(event: Identifier, handler: EventHandler<C, R>) {
            val list = handlers.getOrPut(event) { mutableListOf() }
            list.add(handler)
            list.sortBy { it.priority }
        }

    }

}
