package net.jidb.to.base.client.api.platform

import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

/**
 * A [ClientPlatform]-specific module that defines a cross-platform contract for networking on the client side.
 * This module has code for registering a handler that receives a [CustomPacketPayload], and for sending one to the server.
 *
 * @see net.jidb.to.base.api.platform.NetworkingPlatformModule
 * @since 0.2.0
 */
abstract class NetworkingClientPlatformModule {

    /**
     * Registers the handler that runs on the client when a payload of the given type arrives from the server.
     *
     * @param P The type of the payload being handled.
     * @param type The type of the payload the handler is bound to.
     * @param clientHandler A function that processes the payload, given the payload itself and the [ClientPayloadContext] it arrived in.
     * @since 0.2.0
     */
    abstract fun <P : CustomPacketPayload> registerHandler(type: CustomPacketPayload.Type<P>, clientHandler: ((data: P, context: ClientPayloadContext) -> Unit))

    /**
     * Sends one or more payloads from the client to the server.
     *
     * @param payload The payload to send.
     * @param others Any further payloads to send alongside the first.
     * @since 0.2.0
     */
    abstract fun sendToServer(payload: CustomPacketPayload, vararg others: CustomPacketPayload)

}
