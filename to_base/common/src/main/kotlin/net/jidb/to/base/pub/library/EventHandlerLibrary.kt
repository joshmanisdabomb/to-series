package net.jidb.to.base.pub.library

import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary

/**
 * [SimpleLibrary] implementation that declares an [EventHandler] for each callback the mod attaches, and provides access to those handlers in one place.
 * Declaring a handler here rather than attaching it directly means the attachment happens when the library is built, so handlers are attached in a known order rather than whenever the class holding them first loads.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.5.0
 */
open class EventHandlerLibrary(modid: String) : SimpleLibrary<EventHandler<*, *>>(modid) {

    /**
     * Declares an event handler, attaching it to each of the given events when the library is built.
     *
     * @param E The type of the event handler being declared.
     * @param C The type of the event context the handler is given.
     * @param R The type of the result the handler returns.
     * @param handler The event handler to attach.
     * @param events Suppliers of the events to attach the handler to, taken lazily so that the events themselves need not have been built yet.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.5.0
     */
    operator fun <E : EventHandler<C, R>, C : Any, R : Any> invoke(handler: E, vararg events: () -> Event<C, R>): Library<EventHandler<*, *>, EventHandler<*, *>>.LibraryEntry<E, E> {
        return this.LibraryEntry({
            val handler = it()
            events.forEach { handler.attach(it()) }
            return@LibraryEntry { handler }
        }, { handler })
    }

}
