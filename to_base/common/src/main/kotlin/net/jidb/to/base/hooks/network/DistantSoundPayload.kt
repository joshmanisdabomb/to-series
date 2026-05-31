package net.jidb.to.base.hooks.network

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.network.PayloadEntry
import net.minecraft.core.Holder
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import org.joml.Vector3fc

data class DistantSoundPayload(val event: Holder<SoundEvent>, val source: SoundSource, val position: Vector3fc, val range: Float, val pitch: Float, val delay: Byte): CustomPacketPayload {

    override fun type() = type

    companion object : PayloadEntry<DistantSoundPayload> {
        override val type = CustomPacketPayload.Type<DistantSoundPayload>(Identifier.fromNamespaceAndPath(ToBaseMod.MOD_ID, "distant_sound"))
        override val codec = StreamCodec.composite(
            SoundEvent.STREAM_CODEC,
            DistantSoundPayload::event,
            ByteBufCodecs.idMapper({ SoundSource.entries.getOrElse(it) { SoundSource.MASTER } }, { it.ordinal }),
            DistantSoundPayload::source,
            ByteBufCodecs.VECTOR3F,
            DistantSoundPayload::position,
            ByteBufCodecs.FLOAT,
            DistantSoundPayload::range,
            ByteBufCodecs.FLOAT,
            DistantSoundPayload::pitch,
            ByteBufCodecs.BYTE,
            DistantSoundPayload::delay,
            ::DistantSoundPayload
        ) as StreamCodec<FriendlyByteBuf, DistantSoundPayload>

        override val phase = PayloadEntry.Phase.PLAY
        override val side = PayloadEntry.Side.S2C
    }

}
