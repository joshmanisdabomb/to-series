package net.jidb.to.base.api.helper

import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.EnumProperty

/**
 * Utility object that provides helper methods for [Block] objects.
 * Contains helper methods to simplify block placement logic.
 * @since 0.0.3
 */
object BlockHelper {

    /**
     * Sets the horizontal placement of a block, using the angle of the vector between the player and their placement.
     *
     * @param state The current state of the block being placed.
     * @param context The context of the block placement, providing information such as the player and location.
     * @param property The property representing the horizontal facing direction of the block. Defaults to `HorizontalDirectionalBlock.FACING`.
     * @return The updated block state with the horizontal placement direction set.
     * @since 0.7.0
     */
    fun horizontalPlayerPlacement(state: BlockState, context: BlockPlaceContext, property: EnumProperty<Direction> = HorizontalDirectionalBlock.FACING) = state.setValue(property, context.horizontalDirection.opposite)

    /**
     * Sets the horizontal placement of a block, using the angle of the vector between the player and their placement.
     *
     * @param context The context of the block placement, providing information such as the player and location.
     * @param property The property representing the horizontal facing direction of the block. Defaults to `HorizontalDirectionalBlock.FACING`.
     * @return The updated block state with the horizontal placement direction set.
     * @since 0.7.0
     */
    fun Block.horizontalPlayerPlacement(context: BlockPlaceContext, property: EnumProperty<Direction> = HorizontalDirectionalBlock.FACING) = horizontalPlayerPlacement(defaultBlockState(), context, property)

    /**
     * Sets the horizontal placement of a block, using the side of the clicked block.
     *
     * @param state The current state of the block being placed.
     * @param context The context of the block placement, providing information such as the player and location.
     * @param default The default placement to use when the block is placed on the top or bottom of another block. Defaults to the same rule as [horizontalPlayerPlacement].
     * @param property The property representing the horizontal facing direction of the block. Defaults to `HorizontalDirectionalBlock.FACING`.
     * @return The updated block state with the horizontal placement direction set.
     * @since 0.0.3
     */
    fun horizontalFacePlacement(state: BlockState, context: BlockPlaceContext, default: Direction = context.horizontalDirection.opposite, property: EnumProperty<Direction> = HorizontalDirectionalBlock.FACING) = state.setValue(property, context.clickedFace.let { if (it.axis == Direction.Axis.Y) default else it })

    /**
     * Sets the horizontal placement of a block, using the side of the clicked block.
     *
     * @param context The context of the block placement, providing information such as the player and location.
     * @param default The default placement to use when the block is placed on the top or bottom of another block. Defaults to the same rule as [horizontalPlayerPlacement].
     * @param property The property representing the horizontal facing direction of the block. Defaults to `HorizontalDirectionalBlock.FACING`.
     * @return The updated block state with the horizontal placement direction set.
     * @since 0.0.3
     */
    fun Block.horizontalFacePlacement(context: BlockPlaceContext, default: Direction = context.horizontalDirection.opposite, property: EnumProperty<Direction> = HorizontalDirectionalBlock.FACING) = horizontalFacePlacement(defaultBlockState(), context, default, property)

    /**
     * Sets the directional placement of a block, using the angle of the vector between the player and their placement.
     *
     * @param state The current state of the block being placed.
     * @param context The context of the block placement, providing information such as the player and location.
     * @param property The property representing the facing direction of the block. Defaults to `DirectionalBlock.FACING`.
     * @return The updated block state with the placement direction set.
     * @since 0.0.3
     */
    fun directionalPlayerPlacement(state: BlockState, context: BlockPlaceContext, property: EnumProperty<Direction> = DirectionalBlock.FACING) = state.setValue(property, context.nearestLookingDirection.opposite)

    /**
     * Sets the directional placement of a block, using the angle of the vector between the player and their placement.
     *
     * @param context The context of the block placement, providing information such as the player and location.
     * @param property The property representing the facing direction of the block. Defaults to `DirectionalBlock.FACING`.
     * @return The updated block state with the placement direction set.
     * @since 0.0.3
     */
    fun Block.directionalPlayerPlacement(context: BlockPlaceContext, property: EnumProperty<Direction> = DirectionalBlock.FACING) = directionalPlayerPlacement(defaultBlockState(), context, property)

    /**
     * Sets the directional placement of a block, using the side of the clicked block.
     *
     * @param state The current state of the block being placed.
     * @param context The context of the block placement, providing information such as the player and location.
     * @param property The property representing the facing direction of the block. Defaults to `DirectionalBlock.FACING`.
     * @return The updated block state with the placement direction set.
     * @since 0.0.3
     */
    fun directionalFacePlacement(state: BlockState, context: BlockPlaceContext, property: EnumProperty<Direction> = DirectionalBlock.FACING) = state.setValue(property, context.clickedFace)

    /**
     * Sets the directional placement of a block, using the side of the clicked block.
     *
     * @param context The context of the block placement, providing information such as the player and location.
     * @param property The property representing the facing direction of the block. Defaults to `DirectionalBlock.FACING`.
     * @return The updated block state with the placement direction set.
     * @since 0.0.3
     */
    fun Block.directionalFacePlacement(context: BlockPlaceContext, property: EnumProperty<Direction> = DirectionalBlock.FACING) = directionalFacePlacement(defaultBlockState(), context, property)

    /**
     * Sets the axis placement of a block, using the side of the clicked block.
     *
     * @param state The current state of the block being placed.
     * @param context The context of the block placement, providing information such as the player and location.
     * @param property The property representing the axis of the block. Defaults to `RotatedPillarBlock.AXIS`.
     * @return The updated block state with the placement direction set.
     * @since 0.0.3
     */
    fun pillarPlacement(state: BlockState, context: BlockPlaceContext, property: EnumProperty<Direction.Axis> = RotatedPillarBlock.AXIS) = state.setValue(property, context.clickedFace.axis)

    /**
     * Sets the axis placement of a block, using the side of the clicked block.
     *
     * @param context The context of the block placement, providing information such as the player and location.
     * @param property The property representing the axis of the block. Defaults to `RotatedPillarBlock.AXIS`.
     * @return The updated block state with the placement direction set.
     * @since 0.0.3
     */
    fun Block.pillarPlacement(context: BlockPlaceContext, property: EnumProperty<Direction.Axis> = RotatedPillarBlock.AXIS) = pillarPlacement(defaultBlockState(), context, property)

}
