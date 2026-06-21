package net.jidb.to.base.api.event

data class SimpleEventHandler<C, R>(val handler: (context: C) -> R, override val priority: Int = 0) : EventHandler<C, R>(priority) {

    override fun invoke(context: C): R = handler(context)

}