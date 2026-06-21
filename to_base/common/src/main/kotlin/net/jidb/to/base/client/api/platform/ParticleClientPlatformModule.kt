package net.jidb.to.base.client.api.platform

import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

abstract class ParticleClientPlatformModule {

    abstract fun <O : ParticleOptions> registerProvider(modid: String, type: () -> ParticleType<O>, provider: (sprites: SpriteSet) -> ParticleProvider<O>)

}
