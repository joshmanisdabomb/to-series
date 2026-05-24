package net.jidb.to.base.network

import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface PayloadHandlerRegistry<C : PayloadContext> {

    operator fun <P : CustomPacketPayload> get(type: CustomPacketPayload.Type<P>): ((data: P, context: C) -> Unit)?

}