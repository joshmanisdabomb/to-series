package net.jidb.to.base.data.pub.collection.module.loot

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

/**
 * A [DataCollectionModule] generating an empty loot table, for a block that drops nothing at all.
 * This is not the same as generating no table: a block with no table logs a missing-loot warning, whereas one with an empty table is understood to drop nothing on purpose.
 *
 * @since 0.3.0
 */
class NoopBlockLootDataCollectionModule : DataCollectionModule() {

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) = mapOf(collection.`object` to LootTable.lootTable())

}
