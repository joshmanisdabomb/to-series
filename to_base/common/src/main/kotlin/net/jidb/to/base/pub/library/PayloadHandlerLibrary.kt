package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.network.PayloadContext
import net.jidb.to.base.api.network.PayloadHandlerRegistry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

open class PayloadHandlerLibrary<C : PayloadContext>(modid: String) : SimpleLibrary<PayloadHandlerLibrary<C>.PayloadHandlerEntry<out CustomPacketPayload>>(modid), PayloadHandlerRegistry<C> {

    override fun <P : CustomPacketPayload> get(type: CustomPacketPayload.Type<P>): ((data: P, context: C) -> Unit)? {
        val handler = values.find { it.type == type } ?: return null
        return handler.handler as (data: P, context: C) -> Unit
    }

    inner class PayloadHandlerEntry<P : CustomPacketPayload>(getter: () -> CustomPacketPayload.Type<P>, val handler: (data: P, context: C) -> Unit) {
        val type: CustomPacketPayload.Type<P> by lazy { getter() }
    }

}