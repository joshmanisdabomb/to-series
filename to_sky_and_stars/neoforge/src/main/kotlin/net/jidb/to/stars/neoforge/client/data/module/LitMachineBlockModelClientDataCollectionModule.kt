package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.renderer.block.dispatch.Variant
import net.minecraft.client.renderer.block.dispatch.VariantMutator
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class LitMachineBlockModelClientDataCollectionModule(val unlit: TexturedModel.Provider, val lit: TexturedModel.Provider? = null) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        val off = unlit.create(collection.`object`, event.block.modelOutput)
        val on = (lit ?: unlit.updateTexture {
            val material = it.get(TextureSlot.FRONT)
            it.put(TextureSlot.FRONT, Material(material.sprite.withSuffix("_lit"), material.forceTranslucent))
        }).createWithSuffix(collection.`object`, "_lit", event.block.modelOutput)

        event.block.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(collection.`object`, BlockModelGenerators.variants(Variant(off)))
                .with(PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
                    .select(Direction.NORTH, BlockModelGenerators.NOP)
                    .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
                    .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
                    .select(Direction.WEST, BlockModelGenerators.Y_ROT_270))
                .with(PropertyDispatch.modify(BlockStateProperties.LIT)
                    .select(false, VariantMutator.MODEL.withValue(off))
                    .select(true, VariantMutator.MODEL.withValue(on)))
        )

        return true
    }
}