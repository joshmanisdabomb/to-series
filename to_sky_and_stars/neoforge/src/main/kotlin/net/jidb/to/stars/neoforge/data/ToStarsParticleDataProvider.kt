package net.jidb.to.stars.neoforge.data

import net.jidb.to.base.helper.LibraryHelper.getIdentifier
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider
import java.util.concurrent.CompletableFuture

class ToStarsParticleDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : ParticleDescriptionProvider(output) {

    override fun addDescriptions() {
        spriteSet(ToStarsMod.particles.nuclear_explosion, ToStarsMod.particles.getIdentifier { nuclear_explosion }, 34, false)
    }

}