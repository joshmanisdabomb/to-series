package net.jidb.to.base.client

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.content.ToBaseClientReloadListenerLibrary
import net.jidb.to.base.client.content.ToBaseScreenLibrary
import net.jidb.to.base.client.mod.ToClientMod
import net.jidb.to.base.client.network.handler.DistantSoundPayloadHandler
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.base.network.payload.DistantSoundPayload

object ToBaseClientMod : ToClientMod() {
    override val common get() = ToBaseMod

    override val screens = ToBaseScreenLibrary
    override val reloadListeners = ToBaseClientReloadListenerLibrary

    override fun clientInit() {
        super.clientInit()

        ClientServices.platform.networking.registerHandler(DistantSoundPayload.type, DistantSoundPayloadHandler::handle)
    }
}
