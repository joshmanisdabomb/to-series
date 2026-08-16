package net.jidb.to.base.pub.library

import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary

open class EventHandlerLibrary(modid: String) : SimpleLibrary<EventHandler<*, *>>(modid) {

    operator fun <E : EventHandler<C, R>, C : Any, R : Any> invoke(handler: E, vararg events: () -> Event<C, R>): Library<EventHandler<*, *>, EventHandler<*, *>>.LibraryEntry<E, E> {
        return this.LibraryEntry({
            val handler = it()
            events.forEach { handler.attach(it()) }
            return@LibraryEntry { handler }
        }, { handler })
    }

}
