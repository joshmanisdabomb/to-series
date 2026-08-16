package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.network.PayloadEntry
import net.jidb.to.base.api.network.PayloadHandlerRegistry
import net.jidb.to.base.api.network.ServerPayloadContext
import net.jidb.to.base.service.Services
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

open class PayloadLibrary(modid: String) : SimpleLibrary<PayloadEntry<out CustomPacketPayload>>(modid) {

    override fun getEntryIdentifier(entry: Library<PayloadEntry<out CustomPacketPayload>, PayloadEntry<out CustomPacketPayload>>.LibraryEntry<out PayloadEntry<out CustomPacketPayload>, out PayloadEntry<out CustomPacketPayload>>) = entry.value.type.id

    fun register(serverHandlers: PayloadHandlerRegistry<ServerPayloadContext>?) {
        for (payload in values) {
            registerPayload(payload, serverHandlers)
        }
    }

    private fun <P : CustomPacketPayload> registerPayload(payload: PayloadEntry<P>, handlerRegistry: PayloadHandlerRegistry<ServerPayloadContext>?) {
        Services.platform.networking.register(payload, handlerRegistry?.get(payload.type))
    }

}
