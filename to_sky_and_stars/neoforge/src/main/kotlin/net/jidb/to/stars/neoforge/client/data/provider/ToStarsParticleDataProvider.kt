package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.base.api.helper.LibraryHelper.getIdentifier
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider
import java.util.concurrent.CompletableFuture

/**
 * Generates the particle descriptions of this mod, i.e. which sprites each of its particles is drawn from.
 *
 * @param output Where the generated files are written.
 * @param provider The registries the descriptions are built against.
 */
class ToStarsParticleDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : ParticleDescriptionProvider(output) {

    override fun addDescriptions() {
        spriteSet(ToStarsMod.particles.nuclear_explosion, ToStarsMod.particles.getIdentifier { nuclear_explosion }, 34, false)
        spriteSet(ToStarsMod.particles.steam, ToStarsMod.particles.getIdentifier { steam }, 4, false)
        spriteSet(ToStarsMod.particles.foam, List(8) { Identifier.withDefaultNamespace("geyser_poof_" + it.plus(1).toString().padStart(2, '0')) })
    }

}
