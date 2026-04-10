package net.jidb.to.base.neoforge.client.data.model

import net.jidb.to.base.client.data.model.IExtendedBlockModelGenerators
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.blockstates.MultiPartGenerator
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.renderer.block.model.Variant
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class ExtendedBlockModelGenerators(val models: BlockModelGenerators) : IExtendedBlockModelGenerators {

    override fun createFullRotatedVariantBlock(block: Block) {
        val variant = Variant(TexturedModel.CUBE.create(block, models.modelOutput))
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