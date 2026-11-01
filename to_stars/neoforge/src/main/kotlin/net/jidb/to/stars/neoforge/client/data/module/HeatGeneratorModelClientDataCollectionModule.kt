package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier

/**
 * A [ClientDataCollectionModule] generating the models of a heat generator (solid or fluid) with lit and unlit faces.
 *
 * @property tier Which tier of generator this is, which determine its bottom texture.
 */
class HeatGeneratorModelClientDataCollectionModule(val tier: Int) : LitMachineBlockModelClientDataCollectionModule(unlit(tier), lit(tier)) {

    companion object {
        private fun unlit(tier: Int): TexturedModel.Provider = TexturedModel.ORIENTABLE.updateTexture {
            val bottom = it.get(TextureSlot.BOTTOM)
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/machine_enclosure_${tier}_bottom"), bottom.forceTranslucent))
            val side = it.get(TextureSlot.SIDE)
            it.put(TextureSlot.SIDE, Material(side.sprite.withPath { it.replace("_solid_generator", "_machine_enclosure") }, side.forceTranslucent))
            val top = it.get(TextureSlot.TOP)
            it.put(TextureSlot.TOP, Material(top.sprite.withPath { it.replace("solid_", "").replace("fluid_", "").replace("_top", "") }, top.forceTranslucent))
            val front = it.get(TextureSlot.FRONT)
            it.put(TextureSlot.FRONT, Material(front.sprite.withPath { it.replace("_front", "") }, front.forceTranslucent))
        }

        private fun lit(tier: Int): TexturedModel.Provider = unlit(tier).updateTexture {
            val top = it.get(TextureSlot.TOP)
            it.put(TextureSlot.TOP, Material(top.sprite.withSuffix("_lit"), top.forceTranslucent))
            val front = it.get(TextureSlot.FRONT)
            it.put(TextureSlot.FRONT, Material(front.sprite.withSuffix("_lit"), front.forceTranslucent))
        }
    }

}
