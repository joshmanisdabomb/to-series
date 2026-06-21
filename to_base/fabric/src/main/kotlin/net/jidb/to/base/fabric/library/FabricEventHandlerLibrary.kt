package net.jidb.to.base.fabric.library

import net.fabricmc.fabric.api.event.Event
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary

open class FabricEventHandlerLibrary(modid: String) : SimpleLibrary<Any>(modid) {

    operator fun <T : Any> invoke(event: Event<T>, callback: T): Library<Any, Any>.LibraryEntry<T, T> {
        event.register(callback)
        return invoke(::i) { callback }
    }

}
