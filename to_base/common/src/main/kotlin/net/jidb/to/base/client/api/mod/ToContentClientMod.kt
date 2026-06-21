package net.jidb.to.base.client.api.mod

import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.api.platform.ReloadListenerClientPlatformModule
import net.jidb.to.base.client.pub.library.*
import net.jidb.to.base.pub.library.ReloadListenerLibrary

interface ToContentClientMod {

    val blockEntityRenderers: BlockEntityRendererLibrary? get() = null
    val entityRenderers: EntityRendererLibrary? get() = null
    val payloadHandlers: ClientPayloadHandlerLibrary? get() = null
    val modelLayers: ModelLayerLibrary? get() = null
    val specialModels: SpecialModelLibrary? get() = null
    val particles: ParticleLibrary? get() = null
    val screens: ScreenLibrary? get() = null
    val events: SimpleLibrary<Event<*, *>>? get() = null
    val eventHandlers: SimpleLibrary<EventHandler<*, *>>? get() = null
    val reloadListeners: ReloadListenerLibrary<ReloadListenerClientPlatformModule>? get() = null

}