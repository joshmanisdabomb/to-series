package net.jidb.to.base.data.api.collection.event

import net.jidb.to.base.data.api.ToDataLootHelper
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

/**
 * The [DataCollectionEvent] generating the loot tables of a mod's blocks, i.e. what each one drops when broken.
 * A block has exactly one table, so where two modules name the same block the last one wins rather than the two merging.
 *
 * @param provider The registries the loot tables are built against.
 * @since 0.3.0
 */
class BlockLootDataCollectionEvent(provider: HolderLookup.Provider) : DataCollectionEvent<Map<Block, LootTable.Builder>, Map<Block, LootTable.Builder>, Map<Block, LootTable.Builder>>() {

    /**
     * The helper offering the loot tables a block usually wants, so that a module need not build a common one by hand.
     *
     * @since 0.3.0
     */
    val helper = ToDataLootHelper(provider)

    override fun combineFromModules(results: Iterable<Map<Block, LootTable.Builder>>): Map<Block, LootTable.Builder>? {
        if (results.count() <= 0) return null
        return results.flatMap { it.entries }.groupBy({ it.key }, { it.value }).mapValues { it.value.last() }
    }

    override fun combineFromCollections(results: Iterable<Map<Block, LootTable.Builder>>) = combineFromModules(results)

}
