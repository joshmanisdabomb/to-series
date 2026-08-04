package net.jidb.to.stars.block

import net.jidb.to.base.api.helper.BlockHelper.horizontalFacePlacement
import net.jidb.to.base.api.helper.DirectionHelper.perpendiculars
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.RotorBlockEntity
import net.jidb.to.stars.info.MachineTier
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.redstone.Orientation
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

/**
 * The rotor blades, which turn against the face of the block behind them and drive a turbine placed there.
 *
 * They cannot be placed against a face that will not hold them or beside another set, since two sets side by side would foul each other; where there is no turbine behind them they can be turned by a redstone signal instead.
 * Each set is drawn a half turn out from the one below it, which is what the `alternate` property records.
 *
 * @param properties The block's own properties.
 */
class RotorBlock(properties: Properties) : BaseEntityBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH)
            .setValue(POWERED, false)
            .setValue(alternate, false)
        )
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = RotorBlockEntity(pos, state,)

    override fun <T : BlockEntity> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>) = createTickerHelper(type, ToStarsMod.blockEntities.rotor_blades, RotorBlockEntity::tick)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(HORIZONTAL_FACING, POWERED, alternate).let {}

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val state = horizontalFacePlacement(context)
        val facing = state.getValue(HORIZONTAL_FACING)
        if (isValid(facing.opposite, context.clickedPos, context.level)) {
            return state
                .setValue(alternate, context.clickedPos.y.rem(2) == 0)
                .setValue(POWERED, isRedstonePowered(state, context.clickedPos, context.level))
        }
        return null
    }

    override fun updateShape(state: BlockState, level: LevelReader, ticks: ScheduledTickAccess, pos: BlockPos, directionToNeighbour: Direction, neighbourPos: BlockPos, neighbourState: BlockState, random: RandomSource): BlockState {
        val facing = state.getValue(HORIZONTAL_FACING)
        return if (isValid(facing.opposite, pos, level)) state else Blocks.AIR.defaultBlockState()
    }

    override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, block: Block, orientation: Orientation?, movedByPiston: Boolean) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston)
        val powered = isRedstonePowered(state, pos, level)
        if (powered != state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, powered), 3)
        }
    }

    /**
     * Whether the blades can sit at a position, i.e. whether the face behind them is solid and no other set is beside them.
     *
     * @param back The direction the blades face away from, i.e. towards what they turn against.
     * @param pos The position being asked about.
     * @param level The level the blades are in.
     * @return Returns `true` if the blades can sit there, otherwise `false`.
     */
    fun isValid(back: Direction, pos: BlockPos, level: LevelReader): Boolean {
        val base = pos.relative(back)
        if (!level.getBlockState(base).isFaceSturdy(level, base, back.opposite)) {
            return false
        }
        val perpendiculars = back.perpendiculars
        for (direction in perpendiculars) {
            val other = level.getBlockState(pos.relative(direction))
            if (other.`is`(this)) {
                return false
            }
        }
        return true
    }

    /**
     * The tier of the turbine driving these blades, if there is one behind them at all.
     *
     * @param state The state of the blades.
     * @param pos The position of the blades.
     * @param level The level they are in.
     * @return The tier of the turbine, or `null` where there is no turbine behind them.
     */
    fun getTurbine(state: BlockState, pos: BlockPos, level: Level): MachineTier? {
        val facing = state.getValue(HORIZONTAL_FACING)
        val base = pos.relative(facing.opposite)
        return (level.getBlockState(base).block as? TurbineBlock)?.machine
    }

    /**
     * Whether the blades are being turned by redstone, which only applies where no turbine is driving them.
     *
     * @param state The state of the blades.
     * @param pos The position of the blades.
     * @param level The level they are in.
     * @return Returns `true` if redstone is turning them, otherwise `false`.
     */
    fun isRedstonePowered(state: BlockState, pos: BlockPos, level: Level): Boolean {
        if (getTurbine(state, pos, level) != null) return false
        return level.hasNeighborSignal(pos)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = shapes[state.getValue(HORIZONTAL_FACING)]!!

    override fun rotate(state: BlockState, rotation: Rotation) = state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)))

    override fun mirror(state: BlockState, mirror: Mirror) = state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)))

    override fun codec() = codec

    companion object {

        /**
         * The codec the block is read from a data pack through.
         */
        val codec = simpleCodec(::RotorBlock)

        /**
         * Whether this set of blades is drawn a half turn out from the one below it, so that a stack of them does not all line up.
         */
        val alternate = BooleanProperty.create("alternate")

        /**
         * The collision shape of the blades, for each of the four directions they can face.
         */
        val shapes = Shapes.rotateHorizontal(Shapes.or(
            box(1.0, 1.0, 5.0, 15.0, 15.0, 11.0),
            box(7.0, 7.0, 11.0, 9.0, 9.0, 16.0)
        ))

    }

}
