package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the blockstate of a block that faces any of the six directions and is drawn from a different model for each of the three axes, so that one placed against a ceiling is not simply the upright model turned over.
 *
 * @property top The model drawn where the block faces up.
 * @property side The model drawn where the block faces horizontally.
 * @property bottom The model drawn where the block faces down.
 * @since 0.6.0
 */
class UprightDirectionalBlockModelClientDataCollectionModule(val top: TexturedModel.Provider, val side: TexturedModel.Provider, val bottom: TexturedModel.Provider) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        event.blockEx.createUprightDirectionalBlock(collection.`object`, top, side, bottom)
        return true
    }

}
