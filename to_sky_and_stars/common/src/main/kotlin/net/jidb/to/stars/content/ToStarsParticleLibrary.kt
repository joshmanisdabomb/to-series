package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.PowerParticleOption
import net.minecraft.core.registries.BuiltInRegistries

object ToStarsParticleLibrary : SimpleRegistryLibrary<ParticleType<*>>(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.PARTICLE_TYPE

    val nuclear_explosion by this {
        object : ParticleType<PowerParticleOption>(true) {
            override fun codec() = PowerParticleOption.codec(this)

            override fun streamCodec() = PowerParticleOption.streamCodec(this)
        } }

}