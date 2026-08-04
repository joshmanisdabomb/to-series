package net.jidb.to.base.data.pub.collection.module.loot

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * A [DataCollectionModule] generating the loot table of an ordinary block, which drops one of a single item.
 * This is the default every block is given unless another loot module is declared for it.
 *
 * @property drop The item the block drops, or `null` for the block itself. Defaults to `null`.
 * @since 0.3.0
 */
class SimpleBlockLootDataCollectionModule(val drop: ItemLike? = null) : DataCollectionModule() {

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) = mapOf(collection.`object` to event.helper.simpleBlockLoot(drop ?: collection.`object`))

}
