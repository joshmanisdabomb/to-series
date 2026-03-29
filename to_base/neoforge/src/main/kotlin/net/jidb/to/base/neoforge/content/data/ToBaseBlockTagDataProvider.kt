package net.jidb.to.base.neoforge.content.data

import net.jidb.to.base.ToBaseMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import java.util.concurrent.CompletableFuture

class ToBaseBlockTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : BlockTagsProvider(output, provider, ToBaseMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.tag(BlockTags.MINEABLE_WITH_AXE)
            .add(ToBaseMod.blocks.research_desk)
    }

}