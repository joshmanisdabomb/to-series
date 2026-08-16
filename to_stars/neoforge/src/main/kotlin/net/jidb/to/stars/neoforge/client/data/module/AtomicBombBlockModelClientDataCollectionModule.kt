package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.stars.block.AtomicBombBlock
import net.jidb.to.stars.neoforge.client.data.ToStarsModels
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.renderer.block.dispatch.Variant
import net.minecraft.client.renderer.block.dispatch.VariantMutator
import net.minecraft.core.Direction
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class AtomicBombBlockModelClientDataCollectionModule : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        val atomicBombHead = ToStarsModels.atomicBombHead.create(collection.`object`, event.block.modelOutput)
        val atomicBombMiddle = ToStarsModels.atomicBombMiddle.create(collection.`object`, event.block.modelOutput)
        val atomicBombTail = ToStarsModels.atomicBombTail.create(collection.`object`, event.block.modelOutput)
        event.block.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(collection.`object`, BlockModelGenerators.variants(Variant(atomicBombHead)))
                .with(PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
                    .select(Direction.EAST, BlockModelGenerators.NOP)
                    .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_90)
                    .select(Direction.WEST, BlockModelGenerators.Y_ROT_180)
                    .select(Direction.NORTH, BlockModelGenerators.Y_ROT_270))
                .with(PropertyDispatch.modify(AtomicBombBlock.segment)
                    .select(AtomicBombBlock.AtomicBombSegment.HEAD, VariantMutator.MODEL.withValue(atomicBombHead))
                    .select(AtomicBombBlock.AtomicBombSegment.MIDDLE, VariantMutator.MODEL.withValue(atomicBombMiddle))
                    .select(AtomicBombBlock.AtomicBombSegment.TAIL, VariantMutator.MODEL.withValue(atomicBombTail))
                )
        )
        return true
    }

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        val atomicBombItem = ToStarsModels.atomicBombItemTemplate.create(
            collection.`object`,
            ToStarsModels.atomicBombTexture(collection.`object`.identifier.withPrefix("block/")),
            event.item.modelOutput
        )
        event.item.itemModelOutput.accept(collection.`object`, ItemModelUtils.plainModel(atomicBombItem))
        return true
    }

}
