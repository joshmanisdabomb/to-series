package net.jidb.to.base.client.data.pub.collection.module.model.block

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.BlockModelGeneratorsAccessor
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.world.level.block.Block

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
