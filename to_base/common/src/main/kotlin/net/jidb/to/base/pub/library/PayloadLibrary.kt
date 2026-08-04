package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.network.PayloadEntry
import net.jidb.to.base.api.network.PayloadHandlerRegistry
import net.jidb.to.base.api.network.ServerPayloadContext
import net.jidb.to.base.service.Services
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

/**
 * [SimpleLibrary] implementation that declares a [PayloadEntry] for each custom packet the mod sends, and provides access to those payloads in one place.
 * Registration is not done at build time as it is in a [net.jidb.to.base.api.library.RegistryLibrary], because a payload has to be registered together with the handler that receives it; [register] does both once the handlers are known.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.2.0
 */
open class PayloadLibrary(modid: String) : SimpleLibrary<PayloadEntry<out CustomPacketPayload>>(modid) {

    override fun getEntryIdentifier(entry: Library<PayloadEntry<out CustomPacketPayload>, PayloadEntry<out CustomPacketPayload>>.LibraryEntry<out PayloadEntry<out CustomPacketPayload>, out PayloadEntry<out CustomPacketPayload>>) = entry.value.type.id

    /**
     * Registers every payload of this library with the current platform, along with the server-side handler declared for it.
     *
     * @param serverHandlers The registry to look each payload's server-side handler up in, or `null` where the payload is only ever received on the client.
     * @since 0.4.0
     */
    fun register(serverHandlers: PayloadHandlerRegistry<ServerPayloadContext>?) {
        for (payload in values) {
            registerPayload(payload, serverHandlers)
        }
    }

    /**
     * Registers a single payload with the current platform, along with the server-side handler declared for it.
     * This exists separately from [register] so that the payload's own type parameter is captured, which the loop over the untyped [values] cannot do.
     *
     * @param P The type of the payload being registered.
     * @param payload The payload entry to register.
     * @param handlerRegistry The registry to look the payload's server-side handler up in, or `null` where the payload is only ever received on the client.
     * @since 0.2.0
     */
    private fun <P : CustomPacketPayload> registerPayload(payload: PayloadEntry<P>, handlerRegistry: PayloadHandlerRegistry<ServerPayloadContext>?) {
        Services.platform.networking.register(payload, handlerRegistry?.get(payload.type))
    }

}
