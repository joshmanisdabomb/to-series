package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.base.pub.library.PayloadHandlerLibrary
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

open class ClientPayloadHandlerLibrary(modid: String) : PayloadHandlerLibrary<ClientPayloadContext>(modid) {

    override fun afterBuild(entry: Library<PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<out CustomPacketPayload>, PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<out CustomPacketPayload>>.LibraryEntry<out PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<out CustomPacketPayload>, out PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<out CustomPacketPayload>>) {
        registerPayload(entry.value)
    }

    private fun <P : CustomPacketPayload> registerPayload(payload: PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<P>) {
        ClientServices.platform.networking.registerHandler(payload.type, payload.handler)
    }

}
