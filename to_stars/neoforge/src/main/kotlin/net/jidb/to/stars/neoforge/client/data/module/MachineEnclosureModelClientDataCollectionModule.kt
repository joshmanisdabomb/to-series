package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.mixin.client.BlockModelGeneratorsAccessor
import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block

/**
 * A [ClientDataCollectionModule] generating the models of a machine enclosure.
 *
 * @property tier Which tier of enclosure this is, which determine its bottom texture.
 */
class MachineEnclosureModelClientDataCollectionModule(val tier: Int) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        (event.block as BlockModelGeneratorsAccessor).`to_base$createTrivialBlock`(collection.`object`, TexturedModel.CUBE_TOP_BOTTOM.updateTexture {
            val material = it.get(TextureSlot.BOTTOM)
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/machine_enclosure_${tier}_bottom"), material.forceTranslucent))
        })
        return true
    }

}
