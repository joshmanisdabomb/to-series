package net.jidb.to.base.api.event

import net.minecraft.resources.Identifier

abstract class Event<C, R>(val id: Identifier) {

    fun attach(handler: (context: C) -> R): Event<C, R> {
        attach(id, handler)
        return this
    }
    fun attach(handler: EventHandler<C, R>): Event<C, R> {
        attach(id, handler)
        return this
    }

    fun call(context: C): List<R>? {
        val results = mutableListOf<R>()
        val handlers = handlers[id] ?: return results
        for (handler in handlers) {
            val result = (handler as EventHandler<C, R>)(context)
            if ((result as? EventResult)?.cancelAfter() == true) {
                return null
            }
            results.add(result)
        }
        return results
    }

    companion object {
        private val handlers = mutableMapOf<Identifier, MutableList<EventHandler<*, *>>>()

        fun <C, R> attach(event: Identifier, handler: (context: C) -> R) = attach(event, SimpleEventHandler(handler))
        fun <C, R> attach(event: Identifier, handler: EventHandler<C, R>) {
            val list = handlers.getOrPut(event) { mutableListOf() }
            list.add(handler)
            list.sortBy { it.priority }
        }
    }

}