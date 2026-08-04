package net.jidb.to.base.data.api.collection.event

import net.jidb.to.base.data.api.ToDataLootHelper
import net.minecraft.core.HolderLookup
import net.minecraft.resources.Identifier
import net.minecraft.world.level.storage.loot.LootTable

/**
 * The [DataCollectionEvent] generating the loot tables of a mod that are not a block's own drops, such as a chest table.
 * Tables are keyed by identifier rather than by block, but merge the same way: where two modules name the same table the last one wins.
 *
 * @param provider The registries the loot tables are built against.
 * @see BlockLootDataCollectionEvent
 * @since 0.8.0
 */
class GeneralLootDataCollectionEvent(provider: HolderLookup.Provider) : DataCollectionEvent<Map<Identifier, LootTable.Builder>, Map<Identifier, LootTable.Builder>, Map<Identifier, LootTable.Builder>>() {

    /**
     * The helper offering the loot tables that are usually wanted, so that a module need not build a common one by hand.
     *
     * @since 0.8.0
     */
    val helper = ToDataLootHelper(provider)

    override fun combineFromModules(results: Iterable<Map<Identifier, LootTable.Builder>>): Map<Identifier, LootTable.Builder>? {
        if (results.count() <= 0) return null
        return results.flatMap { it.entries }.groupBy({ it.key }, { it.value }).mapValues { it.value.last() }
    }

    override fun combineFromCollections(results: Iterable<Map<Identifier, LootTable.Builder>>) = combineFromModules(results)

}
