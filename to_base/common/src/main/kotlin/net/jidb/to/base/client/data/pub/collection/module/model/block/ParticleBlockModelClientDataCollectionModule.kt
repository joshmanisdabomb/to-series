package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.BlockModelGeneratorsAccessor
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the model of a block that is not drawn as anything itself but still needs a texture for its breaking and landing particles, such as one rendered entirely by a block entity renderer.
 *
 * @property particle The texture the particles are taken from, or `null` for one named after the block. Defaults to `null`.
 * @since 0.8.0
 */
class ParticleBlockModelClientDataCollectionModule(val particle: Material? = null) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        if (particle != null) {
            (event.block as BlockModelGeneratorsAccessor).`to_base$createAirLikeBlock`(collection.`object`, particle)
        } else {
            (event.block as BlockModelGeneratorsAccessor).`to_base$createParticleOnlyBlock`(collection.`object`, collection.`object`)
        }
        return true
    }

}
