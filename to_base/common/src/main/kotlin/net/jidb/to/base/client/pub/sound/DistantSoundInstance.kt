package net.jidb.to.base.client.pub.sound

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.client.resources.sounds.TickableSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource

/**
 * A sound that can be heard from far further away than vanilla's attenuation would allow, such as an explosion.
 * Attenuation is turned off and the volume worked out from the distance by hand, so that the sound fades out over its own range rather than the sixteen blocks vanilla allows.
 *
 * Because attenuation is off, the sound would otherwise come from every direction at once, so it is repositioned just in front of the camera each tick along the line to where it really came from, which is what keeps it directional.
 *
 * @param event The sound to play.
 * @param source The sound category it plays under, i.e. which volume slider applies.
 * @property ox The x position the sound really comes from.
 * @property oy The y position the sound really comes from.
 * @property oz The z position the sound really comes from.
 * @property range How far away the sound can still be heard, in blocks.
 * @param pitch The pitch to play the sound at.
 * @param random The randomness source vanilla picks between sound variants with.
 * @since 0.4.0
 */
class DistantSoundInstance(event: SoundEvent, source: SoundSource, val ox: Double, val oy: Double, val oz: Double, val range: Float, pitch: Float, random: RandomSource) : SimpleSoundInstance(event, source, Float.MAX_VALUE, pitch, random, ox, oy, oz), TickableSoundInstance {

    init {
        attenuation = SoundInstance.Attenuation.NONE
        calculate()
    }

    override fun isStopped() = false

    override fun tick() {
        calculate()
    }

    /**
     * Works out the volume from how far the camera is from where the sound really came from, and moves the sound to just in front of the camera along that line.
     * Once out of range the sound is pushed far enough away to be silent rather than stopped, so that it starts being heard again if the player comes back within range.
     *
     * @since 0.4.0
     */
    private fun calculate() {
        val cameraPos = Minecraft.getInstance().gameRenderer.mainCamera().position()
        val vector = cameraPos.subtract(ox, oy, oz)
        val distance = vector.length().toFloat()
        volume = range.minus(distance).div(range).coerceIn(0f, 1f)

        val clamp = vector.normalize().scale(if (volume <= 0f) 10000.0 else -10.0)
        x = cameraPos.x + clamp.x
        y = cameraPos.y + clamp.y
        z = cameraPos.z + clamp.z
    }

}
