package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

open class ParticleLibrary(modid: String) : SimpleLibrary<ParticleLibrary.ParticleEntry<out ParticleOptions>>(modid) {

    override fun afterBuild(entry: Library<ParticleEntry<out ParticleOptions>, ParticleEntry<out ParticleOptions>>.LibraryEntry<out ParticleEntry<out ParticleOptions>, out ParticleEntry<out ParticleOptions>>) = entry.value.register()

    inner class ParticleEntry<O : ParticleOptions>(private val type: () -> ParticleType<O>, private val provider: (sprites: SpriteSet) -> ParticleProvider<O>) {

        internal fun register() = ClientServices.platform.particles.registerProvider(modid, type, provider)

    }

}
