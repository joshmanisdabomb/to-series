package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.base.pub.library.PayloadHandlerLibrary
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

/**
 * [PayloadHandlerLibrary] implementation for the handlers that run on the client when a payload arrives from the server.
 * Unlike the server-side handlers, which a [net.jidb.to.base.pub.library.PayloadLibrary] registers alongside the payloads themselves, a client handler is registered on its own as the library is built, because the payload it receives was already registered in common code.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.2.0
 */
open class ClientPayloadHandlerLibrary(modid: String) : PayloadHandlerLibrary<ClientPayloadContext>(modid) {

    override fun afterBuild(entry: Library<PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<out CustomPacketPayload>, PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<out CustomPacketPayload>>.LibraryEntry<out PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<out CustomPacketPayload>, out PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<out CustomPacketPayload>>) {
        registerPayload(entry.value)
    }

    /**
     * Registers a single handler with the client platform.
     * This exists separately from [afterBuild] so that the payload's own type parameter is captured, which the wildcard the entry carries cannot do.
     *
     * @param P The type of the payload being handled.
     * @param payload The handler entry to register.
     * @since 0.2.0
     */
    private fun <P : CustomPacketPayload> registerPayload(payload: PayloadHandlerLibrary<ClientPayloadContext>.PayloadHandlerEntry<P>) {
        ClientServices.platform.networking.registerHandler(payload.type, payload.handler)
    }

}
