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

class NuclearExplosionParticle(level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, power: Float, val sprites: SpriteSet) : SingleQuadParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.first()) {

    init {
        quadSize = random.nextFloat() * 2.5f + 4.5f
        val time = power.mod(1.0f) + floor(power).times(0.5f)
        lifetime = ((random.nextDouble()).pow(1.5).times(25.0) + 10.0 + time.times(25.0)).roundToInt()
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

    class NuclearExplosionParticleProvider(val sprites: SpriteSet) : ParticleProvider<PowerParticleOption> {

        override fun createParticle(options: PowerParticleOption, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, random: RandomSource): NuclearExplosionParticle {
            return NuclearExplosionParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.power, sprites)
        }

    }

}