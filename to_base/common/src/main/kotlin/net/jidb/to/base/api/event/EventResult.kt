package net.jidb.to.base.api.event

/**
 * An interface that can be optionally implemented by the result type `R` of an  [Event].
 * This interface allows an event result to cancel further processing of other event handlers for the same event.
 *
 * @since 0.5.0
 */
interface EventResult {

    /**
     * Determines whether further processing of any event handlers should be cancelled for the current event.
     *
     * @since 0.5.0
     * @return `true` if further event handlers should not be executed, `false` otherwise.
     */
    fun cancelAfter(): Boolean

}
