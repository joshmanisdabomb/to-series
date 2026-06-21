package net.jidb.to.base.client.pub.sound

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.client.resources.sounds.TickableSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource

class DistantSoundInstance(event: SoundEvent, source: SoundSource, val ox: Double, val oy: Double, val oz: Double, val range: Float, pitch: Float, random: RandomSource) : SimpleSoundInstance(event, source, Float.MAX_VALUE, pitch, random, ox, oy, oz), TickableSoundInstance {

    init {
        attenuation = SoundInstance.Attenuation.NONE
        calculate()
    }

    override fun isStopped() = false

    override fun tick() {
        calculate()
    }

    private fun calculate() {
        val cameraPos = Minecraft.getInstance().gameRenderer.mainCamera.position()
        val vector = cameraPos.subtract(ox, oy, oz)
        val distance = vector.length().toFloat()
        volume = range.minus(distance).div(range).coerceIn(0f, 1f)

        val clamp = vector.normalize().scale(if (volume <= 0f) 10000.0 else -10.0)
        x = cameraPos.x + clamp.x
        y = cameraPos.y + clamp.y
        z = cameraPos.z + clamp.z
    }

}