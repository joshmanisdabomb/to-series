package net.jidb.to.stars.network

import net.jidb.to.base.api.network.PayloadEntry
import net.jidb.to.stars.ToStarsMod
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import org.joml.Vector3fc

data class NuclearExplosionPayload(val strength: Float, val origin: Vector3fc, val velocity: Vector3fc) : CustomPacketPayload {

    override fun type() = type

    companion object : PayloadEntry<NuclearExplosionPayload> {

        override val type = CustomPacketPayload.Type<NuclearExplosionPayload>(Identifier.fromNamespaceAndPath(ToStarsMod.MOD_ID, "nuclear_explosion"))
        override val codec = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            NuclearExplosionPayload::strength,
            ByteBufCodecs.VECTOR3F,
            NuclearExplosionPayload::origin,
            ByteBufCodecs.VECTOR3F,
            NuclearExplosionPayload::velocity,
            ::NuclearExplosionPayload
        )

        override val phase = PayloadEntry.Phase.PLAY
        override val side = PayloadEntry.Side.S2C

    }

}
