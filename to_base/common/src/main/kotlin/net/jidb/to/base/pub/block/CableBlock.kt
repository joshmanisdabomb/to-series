package net.jidb.to.base.pub.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

abstract class CableBlock(properties: Properties) : Block(properties) {

    protected abstract val cableWidth: Int
    protected abstract val rimWidth: Int

    protected val shapes by lazy {
        val center = cube(cableWidth.toDouble())
        val connections = Shapes.rotateAll(boxZ(cableWidth.toDouble(), 0.0, 8.0))
        val rim = Shapes.rotateAll(boxZ((cableWidth + rimWidth.times(2)).toDouble(), 0.0, rimWidth.toDouble()))
        getShapeForEachState {
            var shape: VoxelShape = center
            for ((dir, prop) in attachments) {
                if (it.getValue(prop) != CableAttachType.NONE) {
                    shape = Shapes.or(shape, connections[dir]!!)
                    if (it.getValue(prop) == CableAttachType.NODE) {
                        shape = Shapes.or(shape, rim[dir]!!)
                    }
                }
            }
            shape
        }
    }

    init {
        registerDefaultState(stateDefinition.any().apply {
            attachments.forEach { setValue(it.value, CableAttachType.NONE) }
        })
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.run { attachments.forEach { add(it.value) } }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = shapes.apply(state)

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val bp = BlockPos.MutableBlockPos()
        var state = super.getStateForPlacement(context) ?: return null
        for ((dir, prop) in attachments) {
            val pos2 = bp.set(context.clickedPos).move(dir)
            state = state.setValue(prop, shouldConnect(context.level, state, context.clickedPos, dir, context.level.getBlockState(pos2), pos2))
        }
        return state
    }

    override fun updateShape(state: BlockState, level: LevelReader, ticks: ScheduledTickAccess, pos: BlockPos, directionToNeighbour: Direction, neighbourPos: BlockPos, neighbourState: BlockState, random: RandomSource): BlockState {
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random)
            .setValue(attachments[directionToNeighbour]!!, shouldConnect(level, state, pos, directionToNeighbour, neighbourState, neighbourPos))
    }

    abstract fun shouldConnect(level: LevelReader, state: BlockState, pos: BlockPos, direction: Direction, state2: BlockState, pos2: BlockPos): CableAttachType

    companion object {
        val attachments = Direction.entries.associateWith { EnumProperty.create(it.name.lowercase(), CableAttachType::class.java) }
    }

    enum class CableAttachType : StringRepresentable {
        NONE,
        CABLE,
        NODE;

        override fun getSerializedName() = name.lowercase()
    }

}
