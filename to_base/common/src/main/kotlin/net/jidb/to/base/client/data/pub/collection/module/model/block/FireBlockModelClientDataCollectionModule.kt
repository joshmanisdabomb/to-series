package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the model of a fire-like block, i.e. the floor and wall quads that vanilla's own fire is drawn from.
 *
 * @since 0.3.0
 */
class FireBlockModelClientDataCollectionModule : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        event.blockEx.createFire(collection.`object`)
        return true
    }

}
