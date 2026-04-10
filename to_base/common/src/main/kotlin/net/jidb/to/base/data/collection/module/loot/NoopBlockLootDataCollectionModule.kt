package net.jidb.to.base.data.collection.module.loot

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.collection.module.DataCollectionModule
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

class NoopBlockLootDataCollectionModule() : DataCollectionModule() {

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent): Map<Block, LootTable.Builder> {
        return mapOf(collection.`object` to LootTable.lootTable())
    }

}