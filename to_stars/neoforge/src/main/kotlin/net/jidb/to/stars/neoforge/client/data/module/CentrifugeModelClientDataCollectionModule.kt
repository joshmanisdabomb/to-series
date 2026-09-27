package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.neoforge.client.data.ToStarsModels
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier

/**
 * A [ClientDataCollectionModule] generating both the lit and unlit models of a centrifuge.
 *
 * @property tier Which tier of centrifuge this is, which determine its bottom texture.
 */
class CentrifugeModelClientDataCollectionModule(val tier: Int) : LitMachineBlockModelClientDataCollectionModule(ToStarsModels.centrifuge.updateTexture {
    it.put(TextureSlot.BOTTOM, getBottomTexture(tier))
}, ToStarsModels.centrifugeLit.updateTexture {
    it.put(TextureSlot.BOTTOM, getBottomTexture(tier))
}) {

    companion object {
        private fun getBottomTexture(tier: Int) = Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/machine_enclosure_${tier}_bottom"))
    }

}
