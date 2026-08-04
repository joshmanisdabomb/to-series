package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.BlockModelGeneratorsAccessor
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the model of a block with only one state, drawn from a named model template rather than from a plain cube.
 *
 * @property model The model the block is drawn from.
 * @since 0.6.0
 */
class SimpleBlockModelClientDataCollectionModule(val model: TexturedModel.Provider) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        (event.block as BlockModelGeneratorsAccessor).`to_base$createTrivialBlock`(collection.`object`, model)
        return true
    }

}
