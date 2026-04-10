package net.jidb.to.base.client.data.collection.module.model.block

import net.jidb.to.base.client.data.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.mixin.client.BlockModelGeneratorsAccessor
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block

class HorizontalBlockModelClientDataCollectionModule(val model: TexturedModel.Provider? = null) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        (event.block as BlockModelGeneratorsAccessor).`to_base$createHorizontallyRotatedBlock`(collection.`object`, model ?: TexturedModel.ORIENTABLE)
        return true
    }

}