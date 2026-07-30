package net.jidb.to.base.neoforge.data.collection.provider

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block

class CollectionBlockLootDataProvider(private val collections: Iterable<DataCollection<*>>, provider: HolderLookup.Provider) : BlockLootSubProvider(mutableSetOf(), FeatureFlags.DEFAULT_FLAGS, provider) {

    override fun getKnownBlocks() = collections.mapNotNull { it.`object` as? Block }

    override fun generate() {
        val event = BlockLootDataCollectionEvent(registries)
        val tables = event.process(collections)
        tables?.forEach { (block, loot) ->
            add(block, loot)
        }
    }

}