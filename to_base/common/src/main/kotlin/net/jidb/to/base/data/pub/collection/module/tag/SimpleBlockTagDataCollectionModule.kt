package net.jidb.to.base.data.pub.collection.module.tag

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

open class SimpleBlockTagDataCollectionModule(vararg val tags: TagKey<Block>) : DataCollectionModule() {

    override fun generateBlockTags(collection: DataCollection<Block>, event: BlockTagDataCollectionEvent): Map<TagKey<Block>, List<Block>>? {
        return tags.associateWith { listOf(collection.`object`) }
    }

}
