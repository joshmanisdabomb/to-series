package net.jidb.to.stars.network

import net.jidb.to.base.api.network.PayloadEntry
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

/**
 * Tells the client how much energy a block is holding, so that it can be drawn without the whole block entity being sent.
 *
 * @property pos The position of the block.
 * @property energy How much energy it is holding.
 */
data class EnergySyncPayload(val pos: BlockPos, val energy: Long) : CustomPacketPayload {

    override fun type() = type

    companion object : PayloadEntry<EnergySyncPayload> {

        override val type = CustomPacketPayload.Type<EnergySyncPayload>(Identifier.fromNamespaceAndPath(ToStarsMod.MOD_ID, "energy_storage_sync"))
        override val codec = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            EnergySyncPayload::pos,
            ByteBufCodecs.LONG,
            EnergySyncPayload::energy,
            ::EnergySyncPayload
        )

        override val phase = PayloadEntry.Phase.PLAY
        override val side = PayloadEntry.Side.S2C

    }

}
