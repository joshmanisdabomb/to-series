package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile
import net.minecraft.core.HolderLookup
import net.minecraft.data.AtlasIds
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.client.data.SpriteSourceProvider
import java.util.concurrent.CompletableFuture

/**
 * Generates the sprite definitions of this mod, such as atlas entries.
 *
 * @param output Where the generated files are written.
 * @param provider The registries the definitions are built against.
 */
class ToStarsSpriteDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : SpriteSourceProvider(output, provider, ToStarsMod.modid) {

    override fun gather() {
        atlas(AtlasIds.BLOCKS)
            .addSource(SingleFile(Identifier.fromNamespaceAndPath(modid, "entity/block/centrifuge")))
    }

}
