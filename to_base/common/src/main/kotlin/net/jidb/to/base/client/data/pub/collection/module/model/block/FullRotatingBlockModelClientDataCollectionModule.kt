package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the blockstate of a block that faces any of the six directions, turning one model to suit rather than drawing a different one for each.
 *
 * @since 0.3.0
 */
class FullRotatingBlockModelClientDataCollectionModule : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        event.blockEx.createFullRotatedVariantBlock(collection.`object`)
        return true
    }

}
