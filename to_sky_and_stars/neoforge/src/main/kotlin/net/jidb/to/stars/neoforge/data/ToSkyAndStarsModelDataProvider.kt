package net.jidb.to.stars.neoforge.data

import net.jidb.to.stars.ToSkyAndStarsMod
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.data.PackOutput

class ToSkyAndStarsModelDataProvider(output: PackOutput) : ModelProvider(output, ToSkyAndStarsMod.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        blockModels.createHorizontallyRotatedBlock(ToSkyAndStarsMod.blocks.test_block, TexturedModel.ORIENTABLE.updateTexture {
            it.put(TextureSlot.SIDE, modLocation("block/test_block_side"))
            it.put(TextureSlot.TOP, modLocation("block/test_block_top"))
            it.put(TextureSlot.BOTTOM, modLocation("block/test_block_top"))
            it.put(TextureSlot.FRONT, modLocation("block/test_block_front"))
        })

        itemModels.generateFlatItem(ToSkyAndStarsMod.items.test_item, ModelTemplates.FLAT_ITEM)
    }
}