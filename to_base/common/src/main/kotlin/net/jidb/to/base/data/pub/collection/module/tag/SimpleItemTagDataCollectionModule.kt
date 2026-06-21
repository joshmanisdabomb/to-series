package net.jidb.to.base.data.pub.collection.module.tag

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.ItemTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

open class SimpleItemTagDataCollectionModule(vararg val tags: TagKey<Item>) : DataCollectionModule() {

    override fun generateItemTags(collection: DataCollection<out ItemLike>, event: ItemTagDataCollectionEvent): Map<TagKey<Item>, List<Item>>? {
        return tags.associateWith { listOf(collection.`object`.asItem()) }
    }

}
