package net.jidb.to.base.library

import net.jidb.to.base.network.PayloadEntry
import net.jidb.to.base.network.PayloadHandlerRegistry
import net.jidb.to.base.network.ServerPayloadContext
import net.jidb.to.base.service.Services
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

open class PayloadLibrary(modid: String, private val serverHandlers: PayloadHandlerLibrary<ServerPayloadContext>?) : SimpleLibrary<PayloadEntry<out CustomPacketPayload>>(modid) {

    override fun getEntryIdentifier(entry: Library<PayloadEntry<out CustomPacketPayload>, PayloadEntry<out CustomPacketPayload>>.LibraryEntry<out PayloadEntry<out CustomPacketPayload>, out PayloadEntry<out CustomPacketPayload>>) = entry.value.type.id

    override fun afterBuild(entry: Library<PayloadEntry<out CustomPacketPayload>, PayloadEntry<out CustomPacketPayload>>.LibraryEntry<out PayloadEntry<out CustomPacketPayload>, out PayloadEntry<out CustomPacketPayload>>) {
        registerPayload(entry.value, serverHandlers)
    }

    private fun <P : CustomPacketPayload> registerPayload(payload: PayloadEntry<P>, handlerRegistry: PayloadHandlerRegistry<ServerPayloadContext>?) {
        Services.platform.networking.register(payload, handlerRegistry?.get(payload.type))
    }

}