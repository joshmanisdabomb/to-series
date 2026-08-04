package net.jidb.to.base.neoforge.client.data.model

import net.jidb.to.base.client.data.api.model.IExtendedBlockModelGenerators
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.blockstates.MultiPartGenerator
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.renderer.block.dispatch.Variant
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties

/**
 * [IExtendedBlockModelGenerators] implementation for Neoforge, which writes through vanilla's own generator rather than replacing it.
 *
 * @property models The generator the blockstates and models are written through.
 * @since 0.3.0
 */
class ExtendedBlockModelGenerators(val models: BlockModelGenerators) : IExtendedBlockModelGenerators {

    override fun createFullRotatedVariantBlock(block: Block, model: TexturedModel.Provider) {
        val variant = Variant(model.create(block, models.modelOutput))
        models.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(
                block, BlockModelGenerators.variants(
                    variant,
                    variant.with(BlockModelGenerators.X_ROT_90),
                    variant.with(BlockModelGenerators.X_ROT_180),
                    variant.with(BlockModelGenerators.X_ROT_270),
                    variant.with(BlockModelGenerators.Y_ROT_90),
                    variant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_90)),
                    variant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_180)),
                    variant.with(BlockModelGenerators.Y_ROT_90.then(BlockModelGenerators.X_ROT_270)),
                    variant.with(BlockModelGenerators.Y_ROT_180),
                    variant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_90)),
                    variant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_180)),
                    variant.with(BlockModelGenerators.Y_ROT_180.then(BlockModelGenerators.X_ROT_270)),
                    variant.with(BlockModelGenerators.Y_ROT_270),
                    variant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_90)),
                    variant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_180)),
                    variant.with(BlockModelGenerators.Y_ROT_270.then(BlockModelGenerators.X_ROT_270))
                )
            )
        )
    }

    override fun createUprightDirectionalBlock(block: Block, top: TexturedModel.Provider, side: TexturedModel.Provider, bottom: TexturedModel.Provider) {
        val side = side.create(block, models.modelOutput)
        models.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.FACING)
                    .select(Direction.EAST, BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_90))
                    .select(Direction.SOUTH, BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_180))
                    .select(Direction.WEST, BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_270))
                    .select(Direction.NORTH, BlockModelGenerators.plainVariant(side))
                    .select(Direction.UP, BlockModelGenerators.plainVariant(top.create(block, models.modelOutput)))
                    .select(Direction.DOWN, BlockModelGenerators.plainVariant(bottom.create(block, models.modelOutput))))
        )
    }

    override fun createFire(block: Block) {
        val condition = BlockModelGenerators.condition()
            .term(BlockStateProperties.NORTH, false)
            .term(BlockStateProperties.EAST, false)
            .term(BlockStateProperties.SOUTH, false)
            .term(BlockStateProperties.WEST, false)
            .term(BlockStateProperties.UP, false)
        val floor = models.createFloorFireModels(block)
        val side = models.createSideFireModels(block)
        val top = models.createTopFireModels(block)

        models.blockStateOutput.accept(
            MultiPartGenerator.multiPart(block)
                .with(condition, floor)
                .with(BlockModelGenerators.or(
                    BlockModelGenerators.condition()
                        .term(BlockStateProperties.NORTH, true), condition
                ), side)
                .with(BlockModelGenerators.or(
                    BlockModelGenerators.condition()
                        .term(BlockStateProperties.EAST, true), condition
                ), side.with(BlockModelGenerators.Y_ROT_90))
                .with(BlockModelGenerators.or(
                    BlockModelGenerators.condition()
                        .term(BlockStateProperties.SOUTH, true), condition
                ), side.with(BlockModelGenerators.Y_ROT_180))
                .with(BlockModelGenerators.or(
                    BlockModelGenerators.condition()
                        .term(BlockStateProperties.WEST, true), condition
                ), side.with(BlockModelGenerators.Y_ROT_270))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.UP, true), top)
        )
    }

}
