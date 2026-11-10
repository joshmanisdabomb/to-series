package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import kotlin.text.split

/**
 * A [ClientDataCollectionModule] generating the models of a electric furnace with lit and unlit faces.
 *
 * @property tier Which tier of generator this is, which determine its bottom texture.
 */
class EnergyFurnaceModelClientDataCollectionModule(val tier: Int) : LitMachineBlockModelClientDataCollectionModule(unlit(tier), lit(tier)) {

    companion object {
        private fun unlit(tier: Int): TexturedModel.Provider = TexturedModel.ORIENTABLE.updateTexture {
            val bottom = it.get(TextureSlot.BOTTOM)
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/machine_enclosure_${tier}_bottom"), bottom.forceTranslucent))
            val side = it.get(TextureSlot.SIDE)
            it.put(TextureSlot.SIDE, Material(side.sprite.withPath { it.split("_powered_").first() + "_machine_enclosure_side" }, side.forceTranslucent))
            val top = it.get(TextureSlot.TOP)
            it.put(TextureSlot.TOP, Material(top.sprite.withPath { it.split("_powered_").first() + "_machine_enclosure_top" }, top.forceTranslucent))
            val front = it.get(TextureSlot.FRONT)
            it.put(TextureSlot.FRONT, Material(front.sprite.withPath { it.replace("_front", "") }, front.forceTranslucent))
        }

        private fun lit(tier: Int): TexturedModel.Provider = unlit(tier).updateTexture {
            val front = it.get(TextureSlot.FRONT)
            it.put(TextureSlot.FRONT, Material(front.sprite.withSuffix("_lit"), front.forceTranslucent))
        }
    }

}
