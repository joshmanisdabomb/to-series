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
        val atomic_bomb_head = ToStarsModels.ATOMIC_BOMB_HEAD.create(collection.`object`, event.block.modelOutput)
        val atomic_bomb_middle = ToStarsModels.ATOMIC_BOMB_MIDDLE.create(collection.`object`, event.block.modelOutput)
        val atomic_bomb_tail = ToStarsModels.ATOMIC_BOMB_TAIL.create(collection.`object`, event.block.modelOutput)
        event.block.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(collection.`object`, BlockModelGenerators.variants(Variant(atomic_bomb_head)))
                .with(PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
                    .select(Direction.EAST, BlockModelGenerators.NOP)
                    .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_90)
                    .select(Direction.WEST, BlockModelGenerators.Y_ROT_180)
                    .select(Direction.NORTH, BlockModelGenerators.Y_ROT_270))
                .with(PropertyDispatch.modify(AtomicBombBlock.SEGMENT)
                    .select(AtomicBombBlock.AtomicBombSegment.HEAD, VariantMutator.MODEL.withValue(atomic_bomb_head))
                    .select(AtomicBombBlock.AtomicBombSegment.MIDDLE, VariantMutator.MODEL.withValue(atomic_bomb_middle))
                    .select(AtomicBombBlock.AtomicBombSegment.TAIL, VariantMutator.MODEL.withValue(atomic_bomb_tail))
                ))
        return true
    }

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        val atomic_bomb_item = ToStarsModels.TEMPLATE_ATOMIC_BOMB_ITEM.create(
            collection.`object`,
            ToStarsModels.TEXTURES_ATOMIC_BOMB(collection.`object`.identifier.withPrefix("block/")),
            event.item.modelOutput
        )
        event.item.itemModelOutput.accept(collection.`object`, ItemModelUtils.plainModel(atomic_bomb_item))
        return true
    }

}