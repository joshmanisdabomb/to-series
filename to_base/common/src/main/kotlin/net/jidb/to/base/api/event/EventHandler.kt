package net.jidb.to.base.api.event

import net.minecraft.resources.Identifier

abstract class EventHandler<C, R>(open val priority: Int = 0) {

    abstract operator fun invoke(context: C): R

    fun attach(vararg event: Identifier): EventHandler<C, R> {
        event.forEach { Event.attach(it, this) }
        return this
    }
    fun attach(vararg event: Event<C, R>): EventHandler<C, R> {
        event.forEach { Event.attach(it.id, this) }
        return this
    }

}