package net.jidb.to.base.api.network

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

/**
 * An entry for a payload of type [P], which is a specific implementation of [CustomPacketPayload].
 * This interface is to group a packet's [type], [codec], running [phase] and [side] direction, used in [net.jidb.to.base.pub.library.PayloadLibrary].
 *
 * @param P The type of [CustomPacketPayload] associated with this payload entry.
 * @since 0.2.0
 */
interface PayloadEntry<P : CustomPacketPayload> {

    /**
     * The payload type registered with [P] of the [CustomPacketPayload] system.
     *
     * @since 0.2.0
     */
    val type: CustomPacketPayload.Type<P>

    /**
     * The payload codec used to encode and decode [P] over the network with a [FriendlyByteBuf].
     *
     * @since 0.2.0
     */
    val codec: StreamCodec<in FriendlyByteBuf, P>

    /**
     * The phase in which a particular payload entry is intended to operate.
     * This payload can be used for gameplay, configuration, or both.
     *
     * @since 0.2.0
     */
    val phase: Phase

    /**
     * The direction of transport for this payload entry.
     * This payload can be sent client-to-server, server-to-client, or both.
     *
     * @since 0.2.0
     */
    val side: Side

    /**
     * The 'group' of payloads to register this packet under.
     * Neoforge only, unused in Fabric. See [Registering Payloads](https://docs.neoforged.net/docs/networking/payload) for usage.
     *
     * @since 0.2.0
     */
    val registrar: String get() = "1"

    /**
     * Represents the game phase in which a payload can be processed.
     *
     * @since 0.2.0
     */
    enum class Phase {

        /**
         * This phase is for payloads that are sent and received during gameplay.
         *
         * @since 0.2.0
         */
        PLAY,

        /**
         * This phase is for 'configuration' between server and client, see [Using Configuration Tasks](https://docs.neoforged.net/docs/networking/configuration-tasks).
         *
         * @since 0.2.0
         */
        CONFIGURATION,

        /**
         * This phase is for both [PLAY] and [CONFIGURATION].
         *
         * @since 0.2.0
         */
        COMMON

    }

    /**
     * Represents the direction in which a payload is sent.
     *
     * @since 0.2.0
     */
    enum class Side {

        /**
         * This is for payloads that are sent from the client and received by the server.
         *
         * @since 0.2.0
         */
        C2S,

        /**
         * This is for payloads that are sent from the server and received by the client.
         *
         * @since 0.2.0
         */
        S2C,

        /**
         * This is for payloads that can be sent in both directions between client and server, [C2S] and [S2C].
         *
         * @since 0.2.0
         */
        BIDIRECTIONAL

    }

}
