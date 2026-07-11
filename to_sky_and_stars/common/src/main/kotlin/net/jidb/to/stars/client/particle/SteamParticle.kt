package net.jidb.to.stars.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.RisingParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3

class SteamParticle(level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, val sprites: SpriteSet) : RisingParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.first()) {

    private val ceiling by lazy { Vec3(random.nextDouble().times(2).minus(1), 0.0, random.nextDouble().times(2).minus(1)).normalize() }

    init {
        val f = random.nextFloat() * 0.2f + 0.8f
        rCol = f
        gCol = f
        bCol = f
        quadSize = random.nextFloat().times(0.2f).plus(0.3f)
    }

    override fun tick() {
        setAlpha(0.3f.plus(1f.minus(age.toFloat().div(lifetime)).times(0.2f)))

        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) {
            remove()
        } else {
            setSpriteFromAge(sprites)
            yd += 0.003
            this.move(xd, yd, zd)
            if (yd > 0 && y == yo) {
                x += ceiling.x.times(0.025)
                z += ceiling.z.times(0.025)
            }
            xd *= 0.9599999785423279
            yd *= 0.9599999785423279
            zd *= 0.9599999785423279
            if (onGround) {
                xd *= 0.699999988079071
                zd *= 0.699999988079071
            }
        }
    }

    override fun getLayer() = Layer.TRANSLUCENT

    class SteamParticleProvider(val sprites: SpriteSet) : ParticleProvider<SimpleParticleType> {

        override fun createParticle(options: SimpleParticleType, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, random: RandomSource): SteamParticle {
            return SteamParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites)
        }

    }

}