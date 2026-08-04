package net.jidb.to.base.api.network

import net.minecraft.network.protocol.common.custom.CustomPacketPayload

/**
 * An interface for getting the payload handler registered to a [CustomPacketPayload].
 *
 * @param C The type of [PayloadContext] (either client or server depending on handler/destination side) to be passed with the payload when invoking handlers.
 * @since 0.2.0
 */
interface PayloadHandlerRegistry<C : PayloadContext> {

    /**
     * Retrieves the handler function for the specified type of [CustomPacketPayload].
     *
     * @param P The type of [CustomPacketPayload] for which the handler is being retrieved.
     * @param type The specific type of [CustomPacketPayload] whose handler is to be retrieved.
     * @return A function that processes the payload data of type [P] and its associated [PayloadContext], or `null` if no handler is registered for the given type.
     * @since 0.2.0
     */
    operator fun <P : CustomPacketPayload> get(type: CustomPacketPayload.Type<P>): ((data: P, context: C) -> Unit)?

}
