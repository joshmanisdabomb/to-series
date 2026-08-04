package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ParticleLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.particle.FoamParticle.FoamParticleProvider
import net.jidb.to.stars.client.particle.NuclearExplosionParticle.NuclearExplosionParticleProvider
import net.jidb.to.stars.client.particle.SteamParticle.SteamParticleProvider

/**
 * [ParticleLibrary] implementation holding how each of this mod's particles is drawn.
 */
object ToStarsClientParticleLibrary : ParticleLibrary(ToStarsMod.MOD_ID) {

    /**
     * Draws the cloud a nuclear explosion throws up.
     */
    val nuclear_explosion by this { ParticleEntry({ ToStarsMod.particles.nuclear_explosion }, ::NuclearExplosionParticleProvider) }

    /**
     * Draws the steam a boiler gives off.
     */
    val steam by this { ParticleEntry({ ToStarsMod.particles.steam }, ::SteamParticleProvider) }

    /**
     * Draws the foam boiling water throws up.
     */
    val foam by this { ParticleEntry({ ToStarsMod.particles.foam }, ::FoamParticleProvider) }

}
