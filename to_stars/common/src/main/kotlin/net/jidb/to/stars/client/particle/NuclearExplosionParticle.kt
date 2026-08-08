package net.jidb.to.stars.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.PowerParticleOption
import net.minecraft.util.RandomSource
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * One puff of the cloud a nuclear explosion throws up, which lives longer the stronger the blast was and is drawn through its sprites at a rate that slows as it ages.
 *
 * @param level The level the particle is in.
 * @param x Where it starts, along x.
 * @param y Where it starts, along y.
 * @param z Where it starts, along z.
 * @param xSpeed How fast it moves, along x.
 * @param ySpeed How fast it moves, along y.
 * @param zSpeed How fast it moves, along z.
 * @param power How strong the blast was, which decides how long the puff lasts.
 * @property sprites The sprites the particle is drawn from as it ages.
 */
class NuclearExplosionParticle(level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, power: Float, val sprites: SpriteSet) : SingleQuadParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.first()) {

    init {
        quadSize = random.nextFloat() * 2.5f + 4.5f
        val time = power.mod(1.0f) + floor(power).times(0.5f)
        lifetime = ((random.nextDouble()).pow(1.5).times(40.0) + 30.0 + time.times(40.0)).roundToInt()
        xd = xSpeed
        yd = ySpeed
        zd = zSpeed
        friction = 0.95f
        gravity = 0f
    }

    override fun tick() {
        super.tick()

        if (!this.removed) {
            val easedAge = (this.age.toDouble() / this.lifetime).pow(0.75)
            this.setSprite(sprites.get((easedAge * this.lifetime).toInt(), this.lifetime))
        }
    }

    override fun getLayer() = Layer.TRANSLUCENT

    /**
     * Builds a [NuclearExplosionParticle] wherever one is asked for, taking the strength of the blast off the particle's own options.
     *
     * @property sprites The sprites the particle is drawn from as it ages.
     */
    class NuclearExplosionParticleProvider(val sprites: SpriteSet) : ParticleProvider<PowerParticleOption> {

        override fun createParticle(options: PowerParticleOption, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, random: RandomSource) = NuclearExplosionParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.power, sprites)

    }

}
