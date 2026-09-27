package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier

/**
 * A [ClientDataCollectionModule] generating the models of a turbine with lit and unlit side faces.
 *
 * @property tier Which tier of turbine this is, which determine its bottom texture.
 */
class TurbineModelClientDataCollectionModule(val tier: Int) : LitMachineBlockModelClientDataCollectionModule(unlit(tier), lit(tier)) {

    companion object {
        private fun unlit(tier: Int): TexturedModel.Provider = TexturedModel.createDefault({
            val texture = it.identifier.withPrefix("block/")
            TextureMapping()
                .put(TextureSlot.DOWN, Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/machine_enclosure_${tier}_bottom")))
                .put(TextureSlot.EAST, Material(texture.withSuffix("_alt")))
                .put(TextureSlot.WEST, Material(texture.withSuffix("_side")))
                .put(TextureSlot.NORTH, Material(texture.withSuffix("_front")))
                .put(TextureSlot.SOUTH, Material(texture.withPath { it.replace("_turbine", "_power_bank_front") }))
                .put(TextureSlot.UP, Material(texture.withPath { it.replace("_turbine", "_machine_enclosure_top") }))
                .put(TextureSlot.PARTICLE, Material(texture.withPath { it.replace("_turbine", "_machine_enclosure_side") }))
        }, ModelTemplates.CUBE)

        private fun lit(tier: Int): TexturedModel.Provider = unlit(tier).updateTexture {
            val east = it.get(TextureSlot.EAST)
            it.put(TextureSlot.EAST, Material(east.sprite.withSuffix("_lit"), east.forceTranslucent))
            val west = it.get(TextureSlot.WEST)
            it.put(TextureSlot.WEST, Material(west.sprite.withSuffix("_lit"), west.forceTranslucent))
        }
    }

}
