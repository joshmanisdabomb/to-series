package net.jidb.to.stars.block

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.AtomicBombBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.Containers
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FallingBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.redstone.Orientation
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import kotlin.jvm.optionals.getOrNull

/**
 * The atomic bomb, which is three blocks long and lies flat, and is armed through the interface its middle segment opens.
 *
 * Only the middle segment carries a block entity, so every other segment works out where its middle is from which way it is facing and how far along it sits; a redstone signal on any of them sets the whole thing off.
 *
 * @param properties The block's own properties.
 */
class AtomicBombBlock(properties: Properties) : BaseEntityBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(segment, AtomicBombSegment.MIDDLE)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(FACING).add(segment).let {}

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        if (state.getValue(segment) == AtomicBombSegment.MIDDLE) {
            return AtomicBombBlockEntity(pos, state)
        }
        return null
    }

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        if (!level.isClientSide) {
            val entity = getMiddleEntity(level, pos, state).getOrNull() ?: return InteractionResult.SUCCESS
            player.openMenu(state.getMenuProvider(level, entity.blockPos))
            //player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
        }

        return InteractionResult.SUCCESS
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val direction = context.horizontalDirection.opposite
        val state = defaultBlockState().setValue(FACING, direction)
        val p1 = context.clickedPos.relative(direction.clockWise)
        val p2 = context.clickedPos.relative(direction.counterClockWise)
        if (!context.level.getBlockState(p1).canBeReplaced(context) || !context.level.isUnobstructed(state, p1, CollisionContext.empty())) return null
        if (!context.level.getBlockState(p2).canBeReplaced(context) || !context.level.isUnobstructed(state, p2, CollisionContext.empty())) return null
        return state
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)
        if (!level.isClientSide) {
            val direction = state.getValue(FACING)
            level.setBlock(pos.relative(direction.counterClockWise), state.setValue(segment, AtomicBombSegment.HEAD), 3)
            level.setBlock(pos.relative(direction.clockWise), state.setValue(segment, AtomicBombSegment.TAIL), 3)
            level.updateNeighborsAt(pos, Blocks.AIR)
            state.updateNeighbourShapes(level, pos, 3)
        }
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        level.scheduleTick(pos, this, 2)
        super.onPlace(state, level, pos, oldState, notify)
    }

    override fun affectNeighborsAfterRemoval(state: BlockState, level: ServerLevel, pos: BlockPos, moved: Boolean) {
        Containers.updateNeighboursAfterDestroy(state, level, pos)
    }

    override fun updateShape(state: BlockState, level: LevelReader, scheduledTickAccess: ScheduledTickAccess, pos: BlockPos, direction: Direction, neighborPos: BlockPos, neighborState: BlockState, random: RandomSource): BlockState {
        if (direction.axis.isHorizontal) {
            val current = state.getValue(FACING)
            when (state.getValue(segment)) {
                AtomicBombSegment.HEAD -> if (current == direction.counterClockWise && !isSegment(neighborState, current, AtomicBombSegment.MIDDLE)) return Blocks.AIR.defaultBlockState()
                AtomicBombSegment.TAIL -> if (current == direction.clockWise && !isSegment(neighborState, current, AtomicBombSegment.MIDDLE)) return Blocks.AIR.defaultBlockState()
                else -> {
                    if (current == direction.clockWise && !isSegment(neighborState, current, AtomicBombSegment.HEAD)) return Blocks.AIR.defaultBlockState()
                    if (current == direction.counterClockWise && !isSegment(neighborState, current, AtomicBombSegment.TAIL)) return Blocks.AIR.defaultBlockState()
                }
            }
        }
        scheduledTickAccess.scheduleTick(pos, this, 2)
        return state
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (level.isClientSide) return
        if (level.hasNeighborSignal(pos)) {
            getMiddleEntity(level, pos, state).ifPresent {
                it.detonate(null)
            }
        } else {
            val facing = state.getValue(FACING)
            if (pos.y <= level.minY) return
            val middle = getMiddle(pos, state)
            if (!canFall(level, middle) || !canFall(level, middle.relative(facing.counterClockWise)) || !canFall(level, middle.relative(facing.clockWise))) return
            level.getBlockEntity(middle, ToStarsMod.blockEntities.atomic_bomb).ifPresent {
                it.fall()
            }
        }
    }

    /**
     * Whether the bomb would fall from a position, i.e. whether there is nothing under it to hold it up.
     *
     * @param level The level the bomb is in.
     * @param pos The position being asked about.
     * @return Returns `true` if there is nothing holding the bomb up, otherwise `false`.
     */
    private fun canFall(level: Level, pos: BlockPos): Boolean {
        val below = pos.below()
        return level.isEmptyBlock(below) || FallingBlock.isFree(level.getBlockState(below))
    }

    /**
     * Whether a state is a particular segment of this same bomb, i.e. the same block facing the same way.
     *
     * @param state The state being checked.
     * @param facing The direction the bomb faces.
     * @param segment The segment being looked for.
     * @return Returns `true` if the state is that segment, otherwise `false`.
     */
    private fun isSegment(state: BlockState, facing: Direction, segment: AtomicBombSegment) = state.block === this && state.getValue(FACING) == facing && state.getValue(Companion.segment) == segment

    /**
     * Where the middle segment of the bomb a position belongs to sits.
     *
     * @param pos The position of one of its segments.
     * @param state The state at that position.
     * @return The position of the middle segment.
     */
    private fun getMiddle(pos: BlockPos, state: BlockState) = pos.relative(state.getValue(FACING).clockWise, state.getValue(segment).offset)

    /**
     * The block entity of the bomb a position belongs to, which only its middle segment carries.
     *
     * @param level The level the bomb is in.
     * @param pos The position of one of its segments.
     * @param state The state at that position.
     * @return The block entity, or empty where the middle segment is missing.
     */
    private fun getMiddleEntity(level: Level, pos: BlockPos, state: BlockState) = level.getBlockEntity(getMiddle(pos, state), ToStarsMod.blockEntities.atomic_bomb)

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = state.getValue(segment).shapes[state.getValue(FACING).counterClockWise]!!

    override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, block: Block, from: Orientation?, notify: Boolean) {
        if (level.hasNeighborSignal(pos)) {
            getMiddleEntity(level, pos, state).ifPresent {
                it.detonate(null)
            }
        }
    }

    override fun hasAnalogOutputSignal(state: BlockState) = true

    override fun getAnalogOutputSignal(state: BlockState, level: Level, pos: BlockPos, direction: Direction) = AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(getMiddle(pos, state)))

    override fun rotate(state: BlockState, rotation: Rotation) = state.setValue(FACING, rotation.rotate(state.getValue(FACING)))

    override fun mirror(state: BlockState, mirror: Mirror) = state.rotate(mirror.getRotation(state.getValue(FACING)))

    override fun codec() = codec

    companion object {

        /**
         * Which of the three segments of the bomb a block is.
         */
        val segment = EnumProperty.create("segment", AtomicBombSegment::class.java)

        /**
         * The codec the block is read from a data pack through.
         */
        val codec = simpleCodec(::AtomicBombBlock)

        /**
         * Builds one tapered length of the bomb's collision shape, as a stack of boxes narrowing from one width to another, since a shape cannot itself be curved.
         *
         * @param minH How far in the shape starts at the near end.
         * @param minV How far in it ends at the far end.
         * @param depth How long the length is.
         * @return The shape.
         */
        private fun createBodyShape(minH: Double, minV: Double, depth: Double): VoxelShape {
            val bodyWidth = 16.0 - minH.times(2.0)
            val points = Mth.floor(bodyWidth)
            var ret = Shapes.empty()
            for (i in 0..points) {
                val j = i.toDouble().div(points)
                val k = 1 - j
                ret = Shapes.or(ret, box(minH.times(k) + minV.times(j), minH.times(j) + minV.times(k), 0.0, 16.0 - (minH.times(k) + minV.times(j)), 16.0 - (minH.times(j) + minV.times(k)), depth))
            }
            return ret
        }

    }

    /**
     * Enum that defines which part of an atomic bomb a block is, from its nose to its tail.
     *
     * @param shape The collision shape of the segment, facing north.
     */
    enum class AtomicBombSegment(shape: VoxelShape) : StringRepresentable {

        /**
         * The nose of the bomb, which tapers to a point.
         */
        HEAD(Shapes.or(
            createBodyShape(4.6863, 0.0, 12.0).move(0.0, 0.0, 0.25),
            createBodyShape(5.1005, 1.0, 1.0).move(0.0, 0.0, 0.1875),
            createBodyShape(5.5147, 2.0, 1.0).move(0.0, 0.0, 0.125),
            createBodyShape(5.9289, 3.0, 1.0).move(0.0, 0.0, 0.0625),
            createBodyShape(6.3431, 4.0, 1.0)
        )),

        /**
         * The body of the bomb, which is the only segment carrying a block entity.
         */
        MIDDLE(createBodyShape(4.6863, 0.0, 16.0)),

        /**
         * The tail of the bomb, which carries its fins.
         */
        TAIL(Shapes.or(
            box(0.0, 0.0, 8.0, 16.0, 16.0, 16.0),
            box(3.0, 3.0, 0.0, 13.0, 13.0, 8.0),
        ));

        /**
         * How far this segment sits from the middle one, measured along the bomb.
         */
        val offset = 1 - ordinal

        /**
         * The collision shape of this segment, for each of the four directions it can face.
         */
        val shapes = Shapes.rotateHorizontal(shape)

        override fun getSerializedName() = name.lowercase()

    }

}
