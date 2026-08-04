package net.jidb.to.base.data.pub.collection.module.loot

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

/**
 * A [DataCollectionModule] generating a loot table written out by hand, for a block whose drops none of the other loot modules describe.
 *
 * @property builder A function building the table, given the collection and event being generated for.
 * @since 0.3.0
 */
class CustomBlockLootDataCollectionModule(val builder: (collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) -> LootTable.Builder) : DataCollectionModule() {

    /**
     * Creates a module generating a table that is already built, for the common case where it does not depend on the block it is for.
     *
     * @param builder The table to generate.
     * @since 0.3.0
     */
    constructor(builder: LootTable.Builder) : this({ _, _ -> builder })

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) = mapOf(collection.`object` to builder(collection, event))

}
