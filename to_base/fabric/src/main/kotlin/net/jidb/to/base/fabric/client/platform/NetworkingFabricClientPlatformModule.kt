package net.jidb.to.base.fabric.client.platform

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.base.client.api.platform.NetworkingClientPlatformModule
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

/**
 * [NetworkingClientPlatformModule] implementation for Fabric.
 *
 * @since 0.2.0
 */
object NetworkingFabricClientPlatformModule : NetworkingClientPlatformModule() {

    override fun <P : CustomPacketPayload> registerHandler(type: CustomPacketPayload.Type<P>, clientHandler: (P, ClientPayloadContext) -> Unit) {
        ClientPlayNetworking.registerGlobalReceiver(type) { data, context -> clientHandler(data, ClientPayloadContext(context.player())) }
    }

    override fun sendToServer(payload: CustomPacketPayload, vararg others: CustomPacketPayload) {
        ClientPlayNetworking.send(payload)
        for (other in others) {
            ClientPlayNetworking.send(other)
        }
    }

}
