package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.BlockModelGeneratorsAccessor
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the model of a plain cube, textured the same on all six faces from a texture named after the block.
 * This is the default every block is given unless another model module is declared for it.
 *
 * @since 0.3.0
 */
class CubeBlockModelClientDataCollectionModule : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        (event.block as BlockModelGeneratorsAccessor).`to_base$createTrivialCube`(collection.`object`)
        return true
    }

}
