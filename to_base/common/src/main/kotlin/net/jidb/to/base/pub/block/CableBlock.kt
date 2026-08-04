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

/**
 * A block that draws and shapes itself as a cable, connecting to its six neighbours through one [CableAttachType] property each.
 * The shape is a central cube with an arm towards each connected neighbour, plus a wider rim on the arms leading to a node, so that a connection to a machine reads differently from a connection to another cable.
 *
 * What counts as a connection is left to the subclass, which is what [NetworkCableBlock] answers from a block network.
 *
 * @param properties The vanilla block properties.
 * @since 0.6.0
 */
abstract class CableBlock(properties: Properties) : Block(properties) {

    /**
     * How thick the cable itself is, in sixteenths of a block.
     *
     * @since 0.6.0
     */
    protected abstract val cableWidth: Int

    /**
     * How far the rim around a node connection stands out from the cable, in sixteenths of a block.
     *
     * @since 0.6.0
     */
    protected abstract val rimWidth: Int

    /**
     * The collision and outline shape of every state of this block, built once and looked up per state.
     *
     * @since 0.6.0
     */
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

    override fun updateShape(state: BlockState, level: LevelReader, ticks: ScheduledTickAccess, pos: BlockPos, directionToNeighbour: Direction, neighbourPos: BlockPos, neighbourState: BlockState, random: RandomSource) = super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random)
        .setValue(attachments[directionToNeighbour]!!, shouldConnect(level, state, pos, directionToNeighbour, neighbourState, neighbourPos))

    /**
     * Decides how this cable connects to one of its neighbours, which is what gives that direction's property its value.
     *
     * @param level The level the cable is in.
     * @param state The state of the cable itself.
     * @param pos The position of the cable itself.
     * @param direction The direction of the neighbour being considered.
     * @param state2 The state of the neighbour being considered.
     * @param pos2 The position of the neighbour being considered.
     * @return How the cable attaches in that direction.
     * @since 0.6.0
     */
    abstract fun shouldConnect(level: LevelReader, state: BlockState, pos: BlockPos, direction: Direction, state2: BlockState, pos2: BlockPos): CableAttachType

    companion object {

        /**
         * The block state property holding how the cable attaches in each of the six directions, named after that direction.
         *
         * @since 0.6.0
         */
        val attachments = Direction.entries.associateWith { EnumProperty.create(it.name.lowercase(), CableAttachType::class.java) }

    }

    /**
     * Enum that defines how a cable attaches to the block on one of its sides.
     *
     * @see CableBlock.attachments
     * @since 0.6.0
     */
    enum class CableAttachType : StringRepresentable {

        /**
         * Nothing is attached on this side, so no arm is drawn.
         *
         * @since 0.6.0
         */
        NONE,

        /**
         * Another cable is attached on this side, so a plain arm is drawn.
         *
         * @since 0.6.0
         */
        CABLE,

        /**
         * Something the network can carry to is attached on this side, so an arm with a rim on the end is drawn.
         *
         * @since 0.6.0
         */
        NODE;

        override fun getSerializedName() = name.lowercase()

    }

}
