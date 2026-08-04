package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.BlockModelGeneratorsAccessor
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the model of a cube with its own top and bottom textures alongside the one its sides share, as a sandstone block has.
 *
 * @since 0.6.0
 */
class CubeBottomTopBlockModelClientDataCollectionModule : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        (event.block as BlockModelGeneratorsAccessor).`to_base$createTrivialBlock`(collection.`object`, TexturedModel.CUBE_TOP_BOTTOM)
        return true
    }

}
