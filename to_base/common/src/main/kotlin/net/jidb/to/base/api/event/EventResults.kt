package net.jidb.to.base.api.event

data class EventResults<R>(val results: List<R> = emptyList(), val cancelled: Boolean = false)
