package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.stars.block.RotorBlock
import net.jidb.to.stars.client.item.render.RotorSpecialRenderer
import net.jidb.to.stars.neoforge.client.data.ToStarsModels
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.renderer.block.dispatch.Variant
import net.minecraft.client.renderer.block.dispatch.VariantMutator
import net.minecraft.client.renderer.item.properties.select.DisplayContext
import net.minecraft.core.Direction
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class RotorModelClientDataCollectionModule : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        val model = ToStarsModels.ROTOR_BLADES.create(collection.`object`, event.block.modelOutput)
        val alt = ToStarsModels.ROTOR_BLADES_ALT.createWithSuffix(collection.`object`, "_alt", event.block.modelOutput)
        val particle = ModelTemplates.PARTICLE_ONLY.createWithSuffix(collection.`object`, "_powered", TextureMapping.particle(collection.`object`), event.block.modelOutput)
        event.block.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(collection.`object`, BlockModelGenerators.variants(Variant(model)))
                .with(PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
                    .select(Direction.NORTH, BlockModelGenerators.NOP)
                    .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
                    .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
                    .select(Direction.WEST, BlockModelGenerators.Y_ROT_270))
                .with(PropertyDispatch.modify(BlockStateProperties.POWERED, RotorBlock.alternate)
                    .select(false, false, VariantMutator.MODEL.withValue(model))
                    .select(false, true, VariantMutator.MODEL.withValue(alt))
                    .select(true, false, VariantMutator.MODEL.withValue(particle))
                    .select(true, true, VariantMutator.MODEL.withValue(particle)))
        )
        return true
    }

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        val block = (collection.`object` as BlockItem).block
        event.item.itemModelOutput.accept(collection.`object`, ItemModelUtils.select(
            DisplayContext(),
            ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(block), RotorSpecialRenderer.Unbaked(false)),
            ItemModelUtils.`when`(listOf(ItemDisplayContext.GUI), ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(block), RotorSpecialRenderer.Unbaked(true)))
        ))
        return true
    }

}