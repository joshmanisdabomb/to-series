package net.jidb.to.stars.network

import net.jidb.to.base.api.network.PayloadEntry
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class EnergyStorageSyncPayload(val pos: BlockPos, val energy: Long) : CustomPacketPayload {

    override fun type() = type

    companion object : PayloadEntry<EnergyStorageSyncPayload> {
        override val type = CustomPacketPayload.Type<EnergyStorageSyncPayload>(Identifier.fromNamespaceAndPath(ToStarsMod.MOD_ID, "energy_storage_sync"))
        override val codec = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            EnergyStorageSyncPayload::pos,
            ByteBufCodecs.LONG,
            EnergyStorageSyncPayload::energy,
            ::EnergyStorageSyncPayload
        )

        override val phase = PayloadEntry.Phase.PLAY
        override val side = PayloadEntry.Side.S2C
    }

}