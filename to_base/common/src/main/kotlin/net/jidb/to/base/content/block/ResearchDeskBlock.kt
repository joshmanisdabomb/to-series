package net.jidb.to.base.content.block

import net.jidb.to.base.content.inventory.menu.ResearchMenu
import net.jidb.to.base.helper.BlockHelper.horizontalPlacement
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class ResearchDeskBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(segment, ResearchDeskSegment.LEFT)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(FACING).add(segment).let {}

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val direction = context.horizontalDirection
        val state = defaultBlockState().setValue(FACING, direction)
        val pos1 = context.clickedPos.relative(direction.clockWise)
        if (!context.level.getBlockState(pos1).canBeReplaced(context) || !context.level.isUnobstructed(state, pos1, CollisionContext.empty())) {
            val pos2 = context.clickedPos.relative(direction.counterClockWise)
            if (!context.level.getBlockState(pos2).canBeReplaced(context) || !context.level.isUnobstructed(state, pos2, CollisionContext.empty())) {
                return null
            }
            return horizontalPlacement(context).setValue(segment, ResearchDeskSegment.RIGHT)
        }
        return horizontalPlacement(context).setValue(segment, ResearchDeskSegment.LEFT)
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)
        if (!level.isClientSide) {
            val direction = state.getValue(FACING)
            if (state.getValue(segment) == ResearchDeskSegment.LEFT) {
                level.setBlock(pos.relative(direction.counterClockWise), state.setValue(segment, ResearchDeskSegment.RIGHT), 3)
            } else {
                level.setBlock(pos.relative(direction.clockWise), state.setValue(segment, ResearchDeskSegment.LEFT), 3)
            }
            level.updateNeighborsAt(pos, Blocks.AIR)
            state.updateNeighbourShapes(level, pos, 3)
        }
    }

    override fun updateShape(state: BlockState, level: LevelReader, scheduledTickAccess: ScheduledTickAccess, pos: BlockPos, direction: Direction, neighborPos: BlockPos, neighborState: BlockState, random: RandomSource): BlockState {
        if (direction.axis == Direction.Axis.Y) return state
        val current = state.getValue(FACING)
        when (state.getValue(segment)) {
            ResearchDeskSegment.LEFT -> if (current == direction.clockWise && !isSegment(neighborState, current, ResearchDeskSegment.RIGHT)) return Blocks.AIR.defaultBlockState()
            ResearchDeskSegment.RIGHT -> if (current == direction.counterClockWise && !isSegment(neighborState, current, ResearchDeskSegment.LEFT)) return Blocks.AIR.defaultBlockState()
        }
        return state
    }

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        if (!level.isClientSide) {
            player.openMenu(state.getMenuProvider(level, pos))
            //player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
        }

        return InteractionResult.SUCCESS
    }

    override fun getMenuProvider(state: BlockState, level: Level, pos: BlockPos): MenuProvider {
        return SimpleMenuProvider({ id, playerInventory, player -> ResearchMenu(id, playerInventory, ContainerLevelAccess.create(level, pos)) }, name)
    }

    private fun isSegment(state: BlockState, facing: Direction, segment: ResearchDeskSegment) = state.block === this && state.getValue(FACING) == facing && state.getValue(Companion.segment) == segment

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = state.getValue(segment).shapes[state.getValue(FACING)]!!

    public override fun codec() = CODEC

    companion object {
        val segment = EnumProperty.create("segment", ResearchDeskSegment::class.java)
        val CODEC = simpleCodec(::ResearchDeskBlock)
    }

    enum class ResearchDeskSegment(shape: VoxelShape) : StringRepresentable {
        LEFT(Shapes.or(
            box(10.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            box(0.0, 12.0, 0.0, 16.0, 14.0, 16.0),
        )),
        RIGHT(Shapes.or(
            box(0.0, 0.0, 0.0, 12.0, 12.0, 16.0),
            box(0.0, 12.0, 0.0, 16.0, 14.0, 16.0),
        ));

        val shapes = Shapes.rotateHorizontal(shape)

        override fun getSerializedName() = name.lowercase()
    }

}