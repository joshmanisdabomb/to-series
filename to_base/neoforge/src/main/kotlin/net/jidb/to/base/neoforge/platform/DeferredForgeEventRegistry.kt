package net.jidb.to.base.neoforge.platform

import net.neoforged.bus.api.Event
import net.neoforged.bus.api.IEventBus

open class DeferredForgeEventRegistry<E : Event>(val event: Class<E>, val listenerFactory: (modid: String, event: E) -> CollectedEventListener<E> = ::CollectedEventListener) {

    private val callbacks = mutableMapOf<String, MutableList<(event: E) -> Unit>>()

    internal fun register(modid: String, callback: (event: E) -> Unit) {
        callbacks.getOrPut(modid) { mutableListOf() }.add(callback)
    }

    fun handle(modid: String, event: E) {
        val callbacks = callbacks[modid] ?: return
        val listener = listenerFactory(modid, event)
        listener.start()
        listener.run(callbacks)
        listener.end()
    }

    fun addListener(modid: String, bus: IEventBus) {
        bus.addListener(event, { handle(modid, it) })
    }

    open class CollectedEventListener<E : Event>(protected val modid: String, protected val event: E) {

        open fun start() = Unit

        open fun run(callbacks: List<(event: E) -> Unit>) {
            callbacks.forEach { it(event) }
        }

        open fun end() = Unit

    }

}
