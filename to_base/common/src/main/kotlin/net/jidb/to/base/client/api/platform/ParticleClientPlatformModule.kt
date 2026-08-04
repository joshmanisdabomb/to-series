package net.jidb.to.base.client.api.platform

import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

/**
 * A [ClientPlatform]-specific module that defines a cross-platform contract for handling particles.
 * This module has code for registering a [ParticleProvider], which is the client-side half of a particle type registered in common code.
 *
 * @since 0.2.0
 */
abstract class ParticleClientPlatformModule {

    /**
     * Registers a [ParticleProvider] for a specific [ParticleType], which builds the particle instances the client renders.
     *
     * @param O The type of the particle options.
     * @param modid The unique namespace of the mod registering the provider.
     * @param type A [ParticleType] supplier that the provider is bound to.
     * @param provider A function that builds the provider, given the [SpriteSet] the particle draws from.
     * @since 0.2.0
     */
    abstract fun <O : ParticleOptions> registerProvider(modid: String, type: () -> ParticleType<O>, provider: (sprites: SpriteSet) -> ParticleProvider<O>)

}
