package net.jidb.to.base.neoforge.content.data

import net.jidb.to.base.ToBaseMod
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.data.PackOutput

class ToBaseModelDataProvider(output: PackOutput) : ModelProvider(output, ToBaseMod.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        blockModels.createTrivialCube(ToBaseMod.blocks.test_block)
        blockModels.createHorizontallyRotatedBlock(ToBaseMod.blocks.test_block_2, TexturedModel.ORIENTABLE.updateTexture {
            it.put(TextureSlot.SIDE, modLocation("block/test_block_2_side"))
            it.put(TextureSlot.TOP, modLocation("block/test_block_2_top"))
            it.put(TextureSlot.BOTTOM, modLocation("block/test_block_2_top"))
            it.put(TextureSlot.FRONT, modLocation("block/test_block_2_front"))
        })

        itemModels.generateFlatItem(ToBaseMod.items.test_item, ModelTemplates.FLAT_ITEM)
    }
}