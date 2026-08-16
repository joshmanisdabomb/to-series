package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ParticleLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.particle.FoamParticle.FoamParticleProvider
import net.jidb.to.stars.client.particle.NuclearExplosionParticle.NuclearExplosionParticleProvider
import net.jidb.to.stars.client.particle.SteamParticle.SteamParticleProvider

object ToStarsClientParticleLibrary : ParticleLibrary(ToStarsMod.MOD_ID) {

    val nuclear_explosion by this { ParticleEntry({ ToStarsMod.particles.nuclear_explosion }, ::NuclearExplosionParticleProvider) }

    val steam by this { ParticleEntry({ ToStarsMod.particles.steam }, ::SteamParticleProvider) }

    val foam by this { ParticleEntry({ ToStarsMod.particles.foam }, ::FoamParticleProvider) }

}
