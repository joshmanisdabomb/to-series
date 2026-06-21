package net.jidb.to.base.client.data.api

import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.renderer.block.dispatch.Variant
import net.minecraft.client.renderer.block.dispatch.VariantMutator
import net.minecraft.core.Direction

object ToDataClientHelper {

    val NUMERIC_TEXTURES = List(16) { ClientServices.platform.data.createTextureSlot(it.toString()) }

    private val hr: Map<Direction, Map<Direction, List<VariantMutator>>> = mapOf(
        Direction.NORTH to mapOf(
            Direction.NORTH to emptyList(),
            Direction.EAST to listOf(BlockModelGenerators.Y_ROT_90),
            Direction.SOUTH to listOf(BlockModelGenerators.Y_ROT_180),
            Direction.WEST to listOf(BlockModelGenerators.Y_ROT_270),
        )
    )

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

    fun horizontalRotation(direction: Direction, default: Direction = Direction.NORTH) = object : VariantMutator {
        override fun apply(variant: Variant): Variant {
            val mutators = hr[default]!![direction]!!
            var ret = variant
            mutators.forEach { ret = ret.with(it) }
            return ret
        }
    }
    fun directionalRotation(direction: Direction, default: Direction = Direction.UP) = object : VariantMutator {
        override fun apply(variant: Variant): Variant {
            val mutators = dr[default]!![direction]!!
            var ret = variant
            mutators.forEach { ret = ret.with(it) }
            return ret
        }
    }

}