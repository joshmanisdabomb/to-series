package net.jidb.to.base.data.pub.collection.module.loot

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

class SilkBlockLootDataCollectionModule(val drop: ItemLike? = null, val silk: ItemLike? = null) : DataCollectionModule() {

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) = mapOf(collection.`object` to event.helper.silkBlockLoot(silk ?: collection.`object`, drop))

}
