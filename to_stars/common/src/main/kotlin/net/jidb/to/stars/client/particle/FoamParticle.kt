package net.jidb.to.stars.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource

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

    class FoamParticleProvider(val sprites: SpriteSet) : ParticleProvider<SimpleParticleType> {

        override fun createParticle(options: SimpleParticleType, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, random: RandomSource) = FoamParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites)

    }

}
