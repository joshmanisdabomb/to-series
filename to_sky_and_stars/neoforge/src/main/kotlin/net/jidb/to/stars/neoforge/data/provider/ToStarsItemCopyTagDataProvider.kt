package net.jidb.to.stars.neoforge.data.provider

import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider
import java.util.concurrent.CompletableFuture

/**
 * Copies the common block tags of this mod onto the items of those blocks, for the few that the automatic copying does not pair up by name.
 *
 * @param output Where the generated files are written.
 * @param provider The registries the tags are built against.
 * @param blockTags What each block tag actually contains.
 */
class ToStarsItemCopyTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, blockTags: CompletableFuture<TagLookup<Block>>) : BlockTagCopyingItemTagProvider(output, provider, blockTags, ToStarsMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.copyCustom(Identifier.fromNamespaceAndPath("c", "ores/uranium"))
        this.copyCustom(Identifier.fromNamespaceAndPath("c", "storage_blocks/raw_uranium"))
        this.copyCustom(Identifier.fromNamespaceAndPath("c", "storage_blocks/uranium"))
        this.copyCustom(Identifier.fromNamespaceAndPath("c", "storage_blocks/heavy_uranium"))
    }

    /**
     * Copies a block tag onto the item tag of the same name.
     *
     * @param identifier The name both tags share.
     */
    private fun copyCustom(identifier: Identifier) {
        this.copy(TagKey.create(Registries.BLOCK, identifier), TagKey.create(Registries.ITEM, identifier))
    }

}
