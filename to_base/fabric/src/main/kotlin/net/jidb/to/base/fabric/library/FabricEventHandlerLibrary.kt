package net.jidb.to.base.fabric.library

import net.fabricmc.fabric.api.event.Event
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary

/**
 * A [SimpleLibrary] holding the callbacks a mod registers against Fabric's own events, which are declared the same way as everything else so that they are registered in the same order.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.6.0
 */
open class FabricEventHandlerLibrary(modid: String) : SimpleLibrary<Any>(modid) {

    /**
     * Declares a callback against one of Fabric's events, named after the property it is assigned to, in the namespace of this library.
     *
     * @param T The type of the callback the event takes.
     * @param event The Fabric event the callback is registered against.
     * @param callback The callback to register.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.6.0
     */
    operator fun <T : Any> invoke(event: Event<T>, callback: T): Library<Any, Any>.LibraryEntry<T, T> {
        event.register(callback)
        return invoke(::i) { callback }
    }

}
