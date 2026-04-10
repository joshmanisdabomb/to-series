package net.jidb.to.base.data.collection.module.loot

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.collection.module.DataCollectionModule
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

class SimpleBlockLootDataCollectionModule(val drop: ItemLike? = null) : DataCollectionModule() {

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent): Map<Block, LootTable.Builder> {
        return mapOf(collection.`object` to event.helper.simpleBlockLoot(drop ?: collection.`object`))
    }

}