package net.jidb.to.base.helper

import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.Property

object BlockHelper {
    fun horizontalPlacement(state: BlockState, context: BlockPlaceContext, property: EnumProperty<Direction> = HorizontalDirectionalBlock.FACING): BlockState {
        return state.setValue(property, context.horizontalDirection.opposite)
    }
    fun Block.horizontalPlacement(context: BlockPlaceContext, property: EnumProperty<Direction> = HorizontalDirectionalBlock.FACING) = BlockHelper.horizontalPlacement(defaultBlockState(), context, property)

    fun horizontalFacePlacement(state: BlockState, context: BlockPlaceContext, default: Direction = context.horizontalDirection.opposite, property: EnumProperty<Direction> = HorizontalDirectionalBlock.FACING): BlockState {
        return state.setValue(property, context.clickedFace.let { if (it.axis == Direction.Axis.Y) default else it })
    }
    fun Block.horizontalFacePlacement(context: BlockPlaceContext, default: Direction = context.horizontalDirection.opposite, property: EnumProperty<Direction> = HorizontalDirectionalBlock.FACING) = BlockHelper.horizontalFacePlacement(defaultBlockState(), context, default, property)

    fun directionalPlayerPlacement(state: BlockState, context: BlockPlaceContext, property: EnumProperty<Direction> = DirectionalBlock.FACING): BlockState {
        return state.setValue(property, context.nearestLookingDirection.opposite)
    }
    fun Block.directionalPlayerPlacement(context: BlockPlaceContext, property: EnumProperty<Direction> = DirectionalBlock.FACING) = BlockHelper.directionalPlayerPlacement(defaultBlockState(), context, property)

    fun directionalFacePlacement(state: BlockState, context: BlockPlaceContext, property: EnumProperty<Direction> = DirectionalBlock.FACING): BlockState {
        return state.setValue(property, context.clickedFace)
    }
    fun Block.directionalFacePlacement(context: BlockPlaceContext, property: EnumProperty<Direction> = DirectionalBlock.FACING) = BlockHelper.directionalFacePlacement(defaultBlockState(), context, property)

    fun pillarPlacement(state: BlockState, context: BlockPlaceContext, property: EnumProperty<Direction.Axis> = RotatedPillarBlock.AXIS): BlockState {
        return state.setValue(property, context.clickedFace.axis)
    }
    fun Block.pillarPlacement(context: BlockPlaceContext, property: EnumProperty<Direction.Axis> = RotatedPillarBlock.AXIS) = BlockHelper.pillarPlacement(defaultBlockState(), context, property)
}