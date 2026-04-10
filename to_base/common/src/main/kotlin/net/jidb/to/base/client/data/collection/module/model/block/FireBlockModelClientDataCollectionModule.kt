package net.jidb.to.base.client.data.collection.module.model.block

import net.jidb.to.base.client.data.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.collection.DataCollection
import net.minecraft.world.level.block.Block

class FireBlockModelClientDataCollectionModule() : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        event.blockEx.createFire(collection.`object`)
        return true
    }

}