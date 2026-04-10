package net.jidb.to.base.data.collection.module.loot

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.collection.module.DataCollectionModule
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

class CustomBlockLootDataCollectionModule(val builder: (collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) -> LootTable.Builder) : DataCollectionModule() {

    constructor(builder: LootTable.Builder) : this({ _, _ -> builder })

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent): Map<Block, LootTable.Builder> {
        return mapOf(collection.`object` to builder(collection, event))
    }

}