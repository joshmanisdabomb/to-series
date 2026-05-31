package net.jidb.to.base.client.mod

import net.jidb.to.base.client.library.ClientPayloadHandlerLibrary
import net.jidb.to.base.client.library.EntityRendererLibrary
import net.jidb.to.base.client.library.ParticleLibrary
import net.jidb.to.base.client.library.ScreenLibrary
import net.jidb.to.base.client.platform.ReloadListenerClientPlatformModule
import net.jidb.to.base.event.Event
import net.jidb.to.base.event.EventHandler
import net.jidb.to.base.library.ReloadListenerLibrary
import net.jidb.to.base.library.SimpleLibrary

interface ToContentClientMod {

    val entityRenderers: EntityRendererLibrary? get() = null
    val payloadHandlers: ClientPayloadHandlerLibrary? get() = null
    val particles: ParticleLibrary? get() = null
    val screens: ScreenLibrary? get() = null
    val events: SimpleLibrary<Event<*, *>>? get() = null
    val eventHandlers: SimpleLibrary<EventHandler<*, *>>? get() = null
    val reloadListeners: ReloadListenerLibrary<ReloadListenerClientPlatformModule>? get() = null

}