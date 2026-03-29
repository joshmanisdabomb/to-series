package net.jidb.to.stars.neoforge.data

import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags

class ToStarsBlockLootDataProvider(provider: HolderLookup.Provider) : BlockLootSubProvider(mutableSetOf(), FeatureFlags.DEFAULT_FLAGS, provider) {

    override fun getKnownBlocks() = ToStarsMod.blocks.values

    override fun generate() {
        dropWhenSilkTouch(ToStarsMod.blocks.nuclear_waste)
        add(ToStarsMod.blocks.nuclear_fire, noDrop())
    }

}