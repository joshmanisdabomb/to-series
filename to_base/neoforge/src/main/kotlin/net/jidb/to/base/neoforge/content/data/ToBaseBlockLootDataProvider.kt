package net.jidb.to.base.neoforge.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.content.block.ResearchDeskBlock
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags

class ToBaseBlockLootDataProvider(provider: HolderLookup.Provider) : BlockLootSubProvider(mutableSetOf(), FeatureFlags.DEFAULT_FLAGS, provider) {

    override fun getKnownBlocks() = ToBaseMod.blocks.values

    override fun generate() {
        add(ToBaseMod.blocks.test_block, createSingleItemTableWithSilkTouch(ToBaseMod.blocks.test_block, ToBaseMod.items.test_item))
        dropSelf(ToBaseMod.blocks.test_block_2)

        add(ToBaseMod.blocks.research_desk, createSinglePropConditionTable(ToBaseMod.blocks.research_desk, ResearchDeskBlock.SEGMENT, ResearchDeskBlock.ResearchDeskSegment.LEFT));
    }

}