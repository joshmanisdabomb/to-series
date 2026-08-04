package net.jidb.to.base.api.event

import net.minecraft.resources.Identifier

/**
 * An event handler that can process a specific type of [Event].
 *
 * @param C The context type, the input data for the event.
 * @param R The result type, the output of the handler after processing the event.
 * @property priority The priority of the handler, where higher priority handlers are executed earlier.
 * @since 0.5.0
 */
abstract class EventHandler<C, R>(open val priority: Int = 0) {

    /**
     * Invokes the event handler with the provided context, processing the context and producing a result.
     *
     * @param context The context of type [C] that is passed to the event handler for processing.
     * @return result of type [R] produced by the event handler after processing the given context.
     * @since 0.5.0
     */
    abstract operator fun invoke(context: C): R

    /**
     * Attaches the current [EventHandler] to one or more specified [Event] identifiers, to be executed when these events are triggered.
     *
     * @param event One or many [Identifier]s to attach this handler to.
     * @return The current [EventHandler] instance for further chaining.
     * @since 0.5.0
     */
    fun attach(vararg event: Identifier): EventHandler<C, R> {
        event.forEach { Event.attach(it, this) }
        return this
    }

    /**
     * Attaches the current [EventHandler] to one or more specified [Event]s, to be executed when these events are triggered.
     *
     * @param event One or many [Event] objects to attach this handler to.
     * @return The current [EventHandler] instance for further chaining.
     * @since 0.5.0
     */
    fun attach(vararg event: Event<C, R>): EventHandler<C, R> {
        event.forEach { Event.attach(it.id, this) }
        return this
    }

}
