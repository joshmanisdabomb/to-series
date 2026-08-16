package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.ParticleClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent

object ParticleForgeClientPlatformModule : ParticleClientPlatformModule() {

    val registry = DeferredForgeEventRegistry(RegisterParticleProvidersEvent::class.java)

    override fun <O : ParticleOptions> registerProvider(modid: String, type: () -> ParticleType<O>, provider: (sprites: SpriteSet) -> ParticleProvider<O>) {
        registry.register(modid) { event ->
            event.registerSpriteSet(type(), provider)
        }
    }

}
