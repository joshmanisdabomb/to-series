package net.jidb.to.stars.neoforge.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import java.util.concurrent.CompletableFuture

/**
 * Generates the block tags of this mod that a collection can't describe, e.g. tags that only contain vanilla blocks.
 *
 * @param output Where the generated files are written.
 * @param provider The registries the tags are built against.
 *
 * @since 0.2.0
 */
class ToStarsBlockTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : BlockTagsProvider(output, provider, ToBaseMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.tag(ToStarsMod.blockTags.incorrect_for_steel_tool)
            .addTag(BlockTags.NEEDS_DIAMOND_TOOL)
    }

}
