package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block

class UprightDirectionalBlockModelClientDataCollectionModule(val top: TexturedModel.Provider, val side: TexturedModel.Provider, val bottom: TexturedModel.Provider) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        event.blockEx.createUprightDirectionalBlock(collection.`object`, top, side, bottom)
        return true
    }

}
