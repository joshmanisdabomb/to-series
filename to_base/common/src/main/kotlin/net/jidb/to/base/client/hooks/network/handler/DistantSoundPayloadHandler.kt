package net.jidb.to.base.client.hooks.network.handler

import net.jidb.to.base.client.network.ClientPayloadContext
import net.jidb.to.base.client.sound.DistantSoundInstance
import net.jidb.to.base.hooks.network.DistantSoundPayload
import net.minecraft.client.Minecraft

object DistantSoundPayloadHandler {

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
