package net.jidb.to.stars.neoforge.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import java.util.concurrent.CompletableFuture

class ToStarsBlockTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : BlockTagsProvider(output, provider, ToBaseMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(ToStarsMod.blocks.nuclear_waste)

        this.tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
            .add(ToStarsMod.blocks.nuclear_waste)
        this.tag(BlockTags.INCORRECT_FOR_GOLD_TOOL)
            .add(ToStarsMod.blocks.nuclear_waste)
        this.tag(BlockTags.INCORRECT_FOR_STONE_TOOL)
            .add(ToStarsMod.blocks.nuclear_waste)
        this.tag(BlockTags.INCORRECT_FOR_COPPER_TOOL)
            .add(ToStarsMod.blocks.nuclear_waste)
        this.tag(BlockTags.INCORRECT_FOR_IRON_TOOL)
            .add(ToStarsMod.blocks.nuclear_waste)

        this.tag(BlockTags.FIRE)
            .add(ToStarsMod.blocks.nuclear_fire)
        this.tag(BlockTags.REPLACEABLE)
            .add(ToStarsMod.blocks.nuclear_fire)
    }

}