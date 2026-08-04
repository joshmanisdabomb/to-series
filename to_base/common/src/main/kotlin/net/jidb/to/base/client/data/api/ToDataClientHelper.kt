package net.jidb.to.base.client.data.api

import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.renderer.block.dispatch.Variant
import net.minecraft.client.renderer.block.dispatch.VariantMutator
import net.minecraft.core.Direction

/**
 * A helper for the pieces of model generation that vanilla either does not expose or only expresses one direction at a time.
 *
 * See [net.jidb.to.base.data.api.ToDataHelper] for the equivalent that both sides can reach.
 *
 * @since 0.4.0
 */
object ToDataClientHelper {

    /**
     * Texture slots named `0` through `15`, for a model whose faces are filled in by number rather than by role.
     *
     * @since 0.4.0
     */
    val numericTextures = List(16) { ClientServices.platform.data.createTextureSlot(it.toString()) }

    /**
     * The rotations that turn a horizontally facing model from the direction it was drawn in to each of the four it can face.
     *
     * @since 0.6.0
     */
    private val hr: Map<Direction, Map<Direction, List<VariantMutator>>> = mapOf(
        Direction.NORTH to mapOf(
            Direction.NORTH to emptyList(),
            Direction.EAST to listOf(BlockModelGenerators.Y_ROT_90),
            Direction.SOUTH to listOf(BlockModelGenerators.Y_ROT_180),
            Direction.WEST to listOf(BlockModelGenerators.Y_ROT_270),
        )
    )

    /**
     * The rotations that turn a model from the direction it was drawn in to each of the six it can face.
     *
     * @since 0.6.0
     */
    private val dr: Map<Direction, Map<Direction, List<VariantMutator>>> = mapOf(
        Direction.UP to mapOf(
            Direction.UP to emptyList(),
            Direction.NORTH to listOf(BlockModelGenerators.X_ROT_90),
            Direction.EAST to listOf(BlockModelGenerators.X_ROT_90, BlockModelGenerators.Y_ROT_90),
            Direction.SOUTH to listOf(BlockModelGenerators.X_ROT_90, BlockModelGenerators.Y_ROT_180),
            Direction.WEST to listOf(BlockModelGenerators.X_ROT_90, BlockModelGenerators.Y_ROT_270),
            Direction.DOWN to listOf(BlockModelGenerators.X_ROT_180)
        ),
        Direction.NORTH to mapOf(
            Direction.UP to listOf(BlockModelGenerators.X_ROT_270),
            Direction.NORTH to emptyList(),
            Direction.EAST to listOf(BlockModelGenerators.Y_ROT_90),
            Direction.SOUTH to listOf(BlockModelGenerators.Y_ROT_180),
            Direction.WEST to listOf(BlockModelGenerators.Y_ROT_270),
            Direction.DOWN to listOf(BlockModelGenerators.X_ROT_90)
        )
    )

    /**
     * Turns a model to face one of the four horizontal directions.
     *
     * @param direction The direction the model should end up facing.
     * @param default The direction the model was drawn facing. Defaults to [Direction.NORTH].
     * @return A mutator applying the rotation to a variant.
     * @since 0.6.0
     */
    fun horizontalRotation(direction: Direction, default: Direction = Direction.NORTH) = object : VariantMutator {

        override fun apply(variant: Variant): Variant {
            val mutators = hr[default]!![direction]!!
            var ret = variant
            mutators.forEach { ret = ret.with(it) }
            return ret
        }

    }

    /**
     * Turns a model to face any of the six directions.
     *
     * @param direction The direction the model should end up facing.
     * @param default The direction the model was drawn facing, which is only supported as [Direction.UP] or [Direction.NORTH]. Defaults to [Direction.UP].
     * @return A mutator applying the rotation to a variant.
     * @since 0.6.0
     */
    fun directionalRotation(direction: Direction, default: Direction = Direction.UP) = object : VariantMutator {

        override fun apply(variant: Variant): Variant {
            val mutators = dr[default]!![direction]!!
            var ret = variant
            mutators.forEach { ret = ret.with(it) }
            return ret
        }

    }

}
