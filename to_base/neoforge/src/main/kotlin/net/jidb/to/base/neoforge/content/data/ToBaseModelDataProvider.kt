package net.jidb.to.base.neoforge.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.content.block.ResearchDeskBlock
import net.jidb.to.base.helper.IdentifierHelper.identifier
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.renderer.block.model.Variant
import net.minecraft.client.renderer.block.model.VariantMutator
import net.minecraft.data.PackOutput


class ToBaseModelDataProvider(output: PackOutput) : ModelProvider(output, ToBaseMod.MOD_ID) {

    companion object {
        val NUMERIC_TEXTURES = List(16) { TextureSlot.create(it.toString()) }
    }

    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        //Test Blocks
        blockModels.createTrivialCube(ToBaseMod.blocks.test_block)
        blockModels.createHorizontallyRotatedBlock(ToBaseMod.blocks.test_block_2, TexturedModel.ORIENTABLE.updateTexture {
            it.put(TextureSlot.SIDE, modLocation("block/test_block_2_side"))
            it.put(TextureSlot.TOP, modLocation("block/test_block_2_top"))
            it.put(TextureSlot.BOTTOM, modLocation("block/test_block_2_top"))
            it.put(TextureSlot.FRONT, modLocation("block/test_block_2_front"))
        })

        itemModels.generateFlatItem(ToBaseMod.items.test_item, ModelTemplates.FLAT_ITEM)

        //Researcher's Desk
        val research_desk_left = ToBaseModels.RESEARCH_DESK_LEFT.create(ToBaseMod.blocks.research_desk, blockModels.modelOutput)
        val research_desk_right = ToBaseModels.RESEARCH_DESK_RIGHT.create(ToBaseMod.blocks.research_desk, blockModels.modelOutput)
        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(ToBaseMod.blocks.research_desk, BlockModelGenerators.variants(Variant(research_desk_left)))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
                .with(PropertyDispatch.modify(ResearchDeskBlock.segment)
                    .select(ResearchDeskBlock.ResearchDeskSegment.LEFT, VariantMutator.MODEL.withValue(research_desk_left))
                    .select(ResearchDeskBlock.ResearchDeskSegment.RIGHT, VariantMutator.MODEL.withValue(research_desk_right))
        ))
        val research_desk_item = ToBaseModels.TEMPLATE_RESEARCH_DESK_ITEM.create(
            ToBaseMod.blocks.research_desk.asItem(),
            ToBaseModels.TEXTURES_RESEARCH_DESK(ToBaseMod.blocks.research_desk.identifier.withPrefix("block/")),
            itemModels.modelOutput
        )
        itemModels.itemModelOutput.accept(ToBaseMod.blocks.research_desk.asItem(), ItemModelUtils.plainModel(research_desk_item))
    }

}