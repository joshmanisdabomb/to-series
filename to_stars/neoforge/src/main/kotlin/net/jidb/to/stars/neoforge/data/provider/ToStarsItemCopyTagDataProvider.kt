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

class ToStarsItemCopyTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, blockTags: CompletableFuture<TagLookup<Block>>) : BlockTagCopyingItemTagProvider(output, provider, blockTags, ToStarsMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.copyCustom(Identifier.fromNamespaceAndPath("c", "ores/uranium"))
        this.copyCustom(Identifier.fromNamespaceAndPath("c", "storage_blocks/raw_uranium"))
        this.copyCustom(Identifier.fromNamespaceAndPath("c", "storage_blocks/uranium"))
        this.copyCustom(Identifier.fromNamespaceAndPath("c", "storage_blocks/heavy_uranium"))
    }

    private fun copyCustom(identifier: Identifier) {
        this.copy(TagKey.create(Registries.BLOCK, identifier), TagKey.create(Registries.ITEM, identifier))
    }

}
