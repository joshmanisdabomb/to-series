package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.ParticleClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent

/**
 * [ParticleClientPlatformModule] implementation for Neoforge.
 *
 * A provider cannot be registered whenever a mod asks, only while Neoforge is raising its own event, so every declaration is queued in a [DeferredForgeEventRegistry] and played back once that arrives.
 *
 * @since 0.2.0
 */
object ParticleForgeClientPlatformModule : ParticleClientPlatformModule() {

    /**
     * The queued registrations, played back when Neoforge raises the event they are waiting on.
     *
     * @since 0.2.0
     */
    val registry = DeferredForgeEventRegistry(RegisterParticleProvidersEvent::class.java)

    override fun <O : ParticleOptions> registerProvider(modid: String, type: () -> ParticleType<O>, provider: (sprites: SpriteSet) -> ParticleProvider<O>) {
        registry.register(modid) { event ->
            event.registerSpriteSet(type(), provider)
        }
    }

}
