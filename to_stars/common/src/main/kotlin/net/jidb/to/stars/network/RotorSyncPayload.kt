package net.jidb.to.stars.network

import net.jidb.to.base.api.network.PayloadEntry
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class RotorSyncPayload(val pos: BlockPos, val speed: Float) : CustomPacketPayload {

    override fun type() = type

    companion object : PayloadEntry<RotorSyncPayload> {

        override val type = CustomPacketPayload.Type<RotorSyncPayload>(Identifier.fromNamespaceAndPath(ToStarsMod.MOD_ID, "rotor_sync"))
        override val codec = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            RotorSyncPayload::pos,
            ByteBufCodecs.FLOAT,
            RotorSyncPayload::speed,
            ::RotorSyncPayload
        )

        override val phase = PayloadEntry.Phase.PLAY
        override val side = PayloadEntry.Side.S2C

    }

}
