package net.jidb.to.base.api.event

/**
 * A data class containing the list of results for an [Event] fired, in order of [EventHandler] execution.
 * Also includes information on if the event was cancelled early by an [EventResult] or not.
 *
 * @param R The type of the event handler results in the list.
 * @property results A list of results obtained from all executed event handlers, in priority order.
 * @property cancelled Indicates whether the event chain was cancelled by one of the handlers.
 * @since 0.8.0
 */
data class EventResults<R>(val results: List<R> = emptyList(), val cancelled: Boolean = false)
