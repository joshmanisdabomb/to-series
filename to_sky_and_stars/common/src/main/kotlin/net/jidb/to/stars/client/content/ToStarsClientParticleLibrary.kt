package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ParticleLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.particle.NuclearExplosionParticle.NuclearExplosionParticleProvider

object ToStarsClientParticleLibrary : ParticleLibrary(ToStarsMod.MOD_ID) {

    val nuclear_explosion by this { ParticleEntry({ ToStarsMod.particles.nuclear_explosion }, ::NuclearExplosionParticleProvider) }

}
