package net.jidb.to.base.data.api.collection.event

import net.jidb.to.base.data.api.ToDataLootHelper
import net.minecraft.core.HolderLookup
import net.minecraft.resources.Identifier
import net.minecraft.world.level.storage.loot.LootTable

class GeneralLootDataCollectionEvent(provider: HolderLookup.Provider) : DataCollectionEvent<Map<Identifier, LootTable.Builder>, Map<Identifier, LootTable.Builder>, Map<Identifier, LootTable.Builder>>() {

    val helper = ToDataLootHelper(provider)

    override fun combineFromModules(results: Iterable<Map<Identifier, LootTable.Builder>>): Map<Identifier, LootTable.Builder>? {
        if (results.count() <= 0) return null
        return results.flatMap { it.entries }.groupBy({ it.key }, { it.value }).mapValues { it.value.last() }
    }

    override fun combineFromCollections(results: Iterable<Map<Identifier, LootTable.Builder>>) = combineFromModules(results)

}
