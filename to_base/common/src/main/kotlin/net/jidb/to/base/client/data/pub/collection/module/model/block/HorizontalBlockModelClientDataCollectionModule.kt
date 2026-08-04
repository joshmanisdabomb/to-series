package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.BlockModelGeneratorsAccessor
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the blockstate of a block that faces one of the four horizontal directions, turning one model to suit each of them.
 *
 * @property model The model each rotation is drawn from, or `null` for a cube with a front face of its own, as a furnace has. Defaults to `null`.
 * @since 0.3.0
 */
class HorizontalBlockModelClientDataCollectionModule(val model: TexturedModel.Provider? = null) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        (event.block as BlockModelGeneratorsAccessor).`to_base$createHorizontallyRotatedBlock`(collection.`object`, model ?: TexturedModel.ORIENTABLE)
        return true
    }

}
