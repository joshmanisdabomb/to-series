package net.jidb.to.base.data.pub.collection.module.tag

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.ItemTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * A [DataCollectionModule] putting an item into the item tags it is declared with.
 *
 * @property tags The tags to put the item into.
 * @since 0.3.0
 */
open class SimpleItemTagDataCollectionModule(vararg val tags: TagKey<Item>) : DataCollectionModule() {

    override fun generateItemTags(collection: DataCollection<out ItemLike>, event: ItemTagDataCollectionEvent) = tags.associateWith { listOf(collection.`object`.asItem()) }

}
