package net.jidb.to.base.neoforge.client.content.data.module

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.content.block.ResearchDeskBlock
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.neoforge.client.content.data.ToBaseModels
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.renderer.block.dispatch.Variant
import net.minecraft.client.renderer.block.dispatch.VariantMutator
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

class ResearchDeskBlockModelClientDataCollectionModule : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        val research_desk_left = ToBaseModels.RESEARCH_DESK_LEFT.create(ToBaseMod.content.blocks.research_desk, event.block.modelOutput)
        val research_desk_right = ToBaseModels.RESEARCH_DESK_RIGHT.create(ToBaseMod.content.blocks.research_desk, event.block.modelOutput)
        event.block.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(ToBaseMod.content.blocks.research_desk, BlockModelGenerators.variants(Variant(research_desk_left)))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
                .with(PropertyDispatch.modify(ResearchDeskBlock.SEGMENT)
                    .select(ResearchDeskBlock.ResearchDeskSegment.LEFT, VariantMutator.MODEL.withValue(research_desk_left))
                    .select(ResearchDeskBlock.ResearchDeskSegment.RIGHT, VariantMutator.MODEL.withValue(research_desk_right))
                ))
        return true
    }

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        val research_desk_item = ToBaseModels.TEMPLATE_RESEARCH_DESK_ITEM.create(
            collection.`object`,
            ToBaseModels.TEXTURES_RESEARCH_DESK(collection.`object`.identifier.withPrefix("block/")),
            event.item.modelOutput
        )
        event.item.itemModelOutput.accept(collection.`object`, ItemModelUtils.plainModel(research_desk_item))
        return true
    }

}