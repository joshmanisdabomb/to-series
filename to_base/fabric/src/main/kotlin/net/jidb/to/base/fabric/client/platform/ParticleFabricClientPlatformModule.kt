package net.jidb.to.base.fabric.client.platform

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.jidb.to.base.client.platform.ParticleClientPlatformModule
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

object ParticleFabricClientPlatformModule : ParticleClientPlatformModule() {

    override fun <O : ParticleOptions> registerProvider(modid: String, type: () -> ParticleType<O>, provider: (sprites: SpriteSet) -> ParticleProvider<O>) {
        ParticleFactoryRegistry.getInstance().register(type(), provider)
    }

}
