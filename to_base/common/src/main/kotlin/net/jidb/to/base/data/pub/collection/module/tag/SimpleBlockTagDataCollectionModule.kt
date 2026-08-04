package net.jidb.to.base.data.pub.collection.module.tag

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

/**
 * A [DataCollectionModule] putting a block into the block tags it is declared with.
 *
 * @property tags The tags to put the block into.
 * @since 0.3.0
 */
open class SimpleBlockTagDataCollectionModule(vararg val tags: TagKey<Block>) : DataCollectionModule() {

    override fun generateBlockTags(collection: DataCollection<Block>, event: BlockTagDataCollectionEvent) = tags.associateWith { listOf(collection.`object`) }

}
