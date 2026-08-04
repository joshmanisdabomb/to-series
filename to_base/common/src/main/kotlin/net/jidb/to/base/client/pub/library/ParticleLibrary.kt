package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

/**
 * [SimpleLibrary] implementation that registers a [ParticleProvider] against the [ParticleType] it builds particles for, and provides access to those providers in one place.
 * The particle types themselves live in a common library, and are taken here as suppliers so that a provider can be declared without the type having been built yet.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.2.0
 */
open class ParticleLibrary(modid: String) : SimpleLibrary<ParticleLibrary.ParticleEntry<out ParticleOptions>>(modid) {

    override fun afterBuild(entry: Library<ParticleEntry<out ParticleOptions>, ParticleEntry<out ParticleOptions>>.LibraryEntry<out ParticleEntry<out ParticleOptions>, out ParticleEntry<out ParticleOptions>>) = entry.value.register()

    /**
     * A provider declared in the outer [ParticleLibrary], pairing the type of particle it builds with the provider that builds it.
     * The pair is kept in an entry of its own rather than registered directly, so that the particle options' type parameter survives the wildcard the library stores.
     *
     * @param O The type of the particle options.
     * @param type A [ParticleType] supplier that the provider is bound to.
     * @param provider A function that builds the provider, given the [SpriteSet] the particle draws from.
     * @since 0.2.0
     */
    inner class ParticleEntry<O : ParticleOptions>(private val type: () -> ParticleType<O>, private val provider: (sprites: SpriteSet) -> ParticleProvider<O>) {

        /**
         * Registers this provider with the client platform, which the outer library does once the entry has been built.
         *
         * @return [Unit]
         * @since 0.2.0
         */
        internal fun register() = ClientServices.platform.particles.registerProvider(modid, type, provider)

    }

}
