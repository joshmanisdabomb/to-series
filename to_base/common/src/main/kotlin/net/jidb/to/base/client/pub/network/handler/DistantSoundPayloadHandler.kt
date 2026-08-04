package net.jidb.to.base.client.pub.network.handler

import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.base.client.pub.sound.DistantSoundInstance
import net.jidb.to.base.pub.network.DistantSoundPayload
import net.minecraft.client.Minecraft

/**
 * The client-side handler for [DistantSoundPayload], which turns the payload into a [DistantSoundInstance] and queues it.
 *
 * @since 0.4.0
 */
object DistantSoundPayloadHandler {

    /**
     * Plays the sound the payload describes, after the delay it carries.
     *
     * @param data The payload that arrived.
     * @param context The context the payload arrived in.
     * @since 0.4.0
     */
    fun handle(data: DistantSoundPayload, context: ClientPayloadContext) {
        val soundInstance = DistantSoundInstance(
            data.event.value(),
            data.source,
            data.position.x().toDouble(),
            data.position.y().toDouble(),
            data.position.z().toDouble(),
            data.range,
            data.pitch,
            context.player.random,
        )

        Minecraft.getInstance().soundManager.playDelayed(soundInstance, data.delay.toInt())
    }

}
