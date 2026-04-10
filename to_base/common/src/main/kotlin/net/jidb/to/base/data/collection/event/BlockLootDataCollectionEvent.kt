package net.jidb.to.base.data.collection.event

import net.jidb.to.base.data.ToDataLootHelper
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

class BlockLootDataCollectionEvent(provider: HolderLookup.Provider) : DataCollectionEvent<Map<Block, LootTable.Builder>, Map<Block, LootTable.Builder>, Map<Block, LootTable.Builder>>() {

    val helper = ToDataLootHelper(provider)

    override fun combineFromModules(results: Iterable<Map<Block, LootTable.Builder>>): Map<Block, LootTable.Builder>? {
        if (results.count() <= 0) return null
        return results.flatMap { it.entries }.groupBy({ it.key }, { it.value }).mapValues { it.value.last() }
    }

    override fun combineFromCollections(results: Iterable<Map<Block, LootTable.Builder>>) = combineFromModules(results)

}
