package net.jidb.to.base.client.platform

import net.jidb.to.base.client.network.ClientPayloadContext
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

abstract class NetworkingClientPlatformModule {

    abstract fun <P : CustomPacketPayload> registerHandler(type: CustomPacketPayload.Type<P>, clientHandler: ((data: P, context: ClientPayloadContext) -> Unit))

    abstract fun sendToServer(payload: CustomPacketPayload, vararg others: CustomPacketPayload)

}
