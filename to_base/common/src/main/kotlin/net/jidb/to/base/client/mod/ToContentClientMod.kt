package net.jidb.to.base.client.mod

import net.jidb.to.base.client.library.ClientPayloadHandlerLibrary
import net.jidb.to.base.client.library.ParticleLibrary
import net.jidb.to.base.client.library.ScreenLibrary
import net.jidb.to.base.client.platform.ReloadListenerClientPlatformModule
import net.jidb.to.base.library.ReloadListenerLibrary

interface ToContentClientMod {

    val payloadHandlers: ClientPayloadHandlerLibrary? get() = null
    val particles: ParticleLibrary? get() = null
    val screens: ScreenLibrary? get() = null
    val reloadListeners: ReloadListenerLibrary<ReloadListenerClientPlatformModule>? get() = null

}