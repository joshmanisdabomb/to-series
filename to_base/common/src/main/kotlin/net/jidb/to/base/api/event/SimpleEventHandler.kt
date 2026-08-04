package net.jidb.to.base.api.event

/**
 * Simple class of an [EventHandler] given to [Event.attach] as a simple lambda function.
 *
 * @param C The context type, the input data for the event.
 * @param R The result type, the output of the handler after processing the event.
 * @property handler A function that takes a context of type [C] and processes it to produce a result of type [R].
 * @property priority The priority of the handler, where higher priority handlers are executed earlier.
 * @since 0.5.0
 */
data class SimpleEventHandler<C, R>(val handler: (context: C) -> R, override val priority: Int = 0) : EventHandler<C, R>(priority) {

    override fun invoke(context: C): R = handler(context)

}
