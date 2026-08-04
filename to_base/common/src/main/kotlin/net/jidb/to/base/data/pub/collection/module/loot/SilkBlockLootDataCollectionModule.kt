package net.jidb.to.base.data.pub.collection.module.loot

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * A [DataCollectionModule] generating the loot table of a block that drops differently under silk touch, as glass and leaves do.
 *
 * @property drop The item dropped without silk touch, or `null` to drop nothing. Defaults to `null`.
 * @property silk The item dropped under silk touch, or `null` for the block itself. Defaults to `null`.
 * @since 0.3.0
 */
class SilkBlockLootDataCollectionModule(val drop: ItemLike? = null, val silk: ItemLike? = null) : DataCollectionModule() {

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) = mapOf(collection.`object` to event.helper.silkBlockLoot(silk ?: collection.`object`, drop))

}
