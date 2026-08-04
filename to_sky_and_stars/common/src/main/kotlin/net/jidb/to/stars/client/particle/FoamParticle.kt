package net.jidb.to.stars.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource

/**
 * The foam boiling water throws up, which shrinks away as it ages.
 *
 * @param level The level the particle is in.
 * @param x Where it starts, along x.
 * @param y Where it starts, along y.
 * @param z Where it starts, along z.
 * @param xSpeed How fast it moves, along x.
 * @param ySpeed How fast it moves, along y.
 * @param zSpeed How fast it moves, along z.
 * @property sprites The sprites the particle is drawn from as it ages.
 */
class FoamParticle(level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, val sprites: SpriteSet) : SingleQuadParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.first()) {

    init {
        this.gravity = 0.75F
        this.friction = 0.999F
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F
        this.lifetime = (16.0 / (this.random.nextFloat() * 0.8 + 0.2)).toInt()
        setSize(0.1f, 0.1f)
    }

    override fun getLayer() = Layer.OPAQUE

    override fun getQuadSize(a: Float): Float {
        val s = (this.age + a) / this.lifetime
        return this.quadSize * (1.0f - s * s)
    }

    override fun tick() {
        super.tick()
        setSpriteFromAge(sprites)
    }

    /**
     * Builds a [FoamParticle] wherever one is asked for.
     *
     * @property sprites The sprites the particle is drawn from as it ages.
     */
    class FoamParticleProvider(val sprites: SpriteSet) : ParticleProvider<SimpleParticleType> {

        override fun createParticle(options: SimpleParticleType, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, random: RandomSource) = FoamParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites)

    }

}
