package net.jidb.to.stars.neoforge.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.stars.content.ToStarsItemTagLibrary
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ItemTagsProvider
import java.util.concurrent.CompletableFuture

/**
 * Generates the item tags of this mod that no collection describes.
 *
 * @param output Where the generated files are written.
 * @param provider The registries the tags are built against.
 */
class ToStarsItemTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : ItemTagsProvider(output, provider, ToBaseMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.tag(ToStarsItemTagLibrary.heat_generator_2x)
            .addTag(ItemTags.COALS)
            .addTag(Tags.Items.STORAGE_BLOCKS_COAL)
    }

}
