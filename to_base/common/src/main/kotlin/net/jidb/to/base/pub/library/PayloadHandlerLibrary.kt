package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.network.PayloadContext
import net.jidb.to.base.api.network.PayloadHandlerRegistry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

/**
 * [SimpleLibrary] implementation that declares the handler run for each custom packet the mod receives, and provides access to those handlers in one place.
 * A library is written per context type, so the handlers that run on the server and the handlers that run on the client stay apart, and a [PayloadLibrary] is given the one that matches the side it is registering for.
 *
 * @param C The type of the context each handler is given, which decides the side these handlers run on.
 * @param modid The mod ID associated with the library.
 * @since 0.2.0
 */
open class PayloadHandlerLibrary<C : PayloadContext>(modid: String) : SimpleLibrary<PayloadHandlerLibrary<C>.PayloadHandlerEntry<out CustomPacketPayload>>(modid), PayloadHandlerRegistry<C> {

    override fun <P : CustomPacketPayload> get(type: CustomPacketPayload.Type<P>): ((data: P, context: C) -> Unit)? {
        val handler = values.find { it.type == type } ?: return null
        return handler.handler as (data: P, context: C) -> Unit
    }

    /**
     * A handler declared in the outer [PayloadHandlerLibrary], pairing the type of packet it receives with the function that processes it.
     * The type is supplied lazily rather than given directly, so that a handler can be declared before the [PayloadLibrary] holding its payload has been built.
     *
     * @param P The type of the payload this handler receives.
     * @param getter A supplier of the type of payload this handler receives.
     * @property handler The function that processes the payload, given the payload itself and the context it arrived in.
     * @since 0.2.0
     */
    inner class PayloadHandlerEntry<P : CustomPacketPayload>(getter: () -> CustomPacketPayload.Type<P>, val handler: (data: P, context: C) -> Unit) {

        /**
         * The type of payload this handler receives, resolved on first use.
         *
         * @since 0.2.0
         */
        val type: CustomPacketPayload.Type<P> by lazy { getter() }

    }

}
