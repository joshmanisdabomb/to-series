package net.jidb.to.base.network

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface PayloadEntry<P : CustomPacketPayload> {

    val type: CustomPacketPayload.Type<P>
    val codec: StreamCodec<in FriendlyByteBuf, P>

    val phase: Phase
    val side: Side

    val registrar: String get() = "1"

    enum class Phase {
        PLAY,
        CONFIGURATION,
        COMMON
    }

    enum class Side {
        C2S,
        S2C,
        BIDIRECTIONAL
    }

}
