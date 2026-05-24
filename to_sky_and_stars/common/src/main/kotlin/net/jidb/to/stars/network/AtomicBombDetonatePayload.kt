package net.jidb.to.stars.network

import net.jidb.to.base.network.PayloadEntry
import net.jidb.to.stars.ToStarsMod
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class AtomicBombDetonatePayload private constructor() : CustomPacketPayload {

    override fun type() = type

    companion object : PayloadEntry<AtomicBombDetonatePayload> {
        val instance = AtomicBombDetonatePayload()

        override val type = CustomPacketPayload.Type<AtomicBombDetonatePayload>(Identifier.fromNamespaceAndPath(ToStarsMod.MOD_ID, "atomic_bomb_detonate"))
        override val codec: StreamCodec<FriendlyByteBuf, AtomicBombDetonatePayload> = StreamCodec.unit(instance)

        override val phase = PayloadEntry.Phase.PLAY
        override val side = PayloadEntry.Side.C2S
    }

}

