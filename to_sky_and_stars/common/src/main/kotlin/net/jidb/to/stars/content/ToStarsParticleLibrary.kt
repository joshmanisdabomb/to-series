package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.pub.particle.AccessibleSimpleParticleType
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.PowerParticleOption
import net.minecraft.core.registries.BuiltInRegistries

/**
 * [SimpleRegistryLibrary] implementation holding the particle types of this mod.
 */
object ToStarsParticleLibrary : SimpleRegistryLibrary<ParticleType<*>>(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.PARTICLE_TYPE

    /**
     * The cloud a nuclear explosion throws up, which carries how strong the blast was so that the particle can be drawn to suit.
     */
    val nuclear_explosion by this {
        object : ParticleType<PowerParticleOption>(true) {

            override fun codec() = PowerParticleOption.codec(this)

            override fun streamCodec() = PowerParticleOption.streamCodec(this)

        }
    }

    /**
     * The steam a boiler gives off.
     */
    val steam by this { AccessibleSimpleParticleType(false) }

    /**
     * The foam that boiling water throws up.
     */
    val foam by this { AccessibleSimpleParticleType(false) }

}
