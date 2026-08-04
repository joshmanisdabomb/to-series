package net.jidb.to.base.pub.network

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.network.PayloadEntry
import net.minecraft.core.Holder
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import org.joml.Vector3fc

/**
 * The payload telling a client to play a sound that can be heard far beyond vanilla's own range, such as an explosion.
 * Vanilla's sound packet is only sent to players near the source, so a sound like this has to be sent deliberately and positioned by the client each frame, which [net.jidb.to.base.client.pub.sound.DistantSoundInstance] does.
 *
 * @property event The sound to play.
 * @property source The sound category it plays under, i.e. which volume slider applies.
 * @property position Where in the world the sound comes from.
 * @property range How far away the sound can still be heard, in blocks.
 * @property pitch The pitch to play the sound at.
 * @property delay How many ticks to wait before playing the sound, which is what puts the rumble after the flash.
 * @since 0.4.0
 */
data class DistantSoundPayload(val event: Holder<SoundEvent>, val source: SoundSource, val position: Vector3fc, val range: Float, val pitch: Float, val delay: Byte) : CustomPacketPayload {

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
