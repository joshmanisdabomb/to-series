package net.jidb.to.base.content.block

import net.jidb.to.base.api.helper.BlockHelper.horizontalPlacement
import net.jidb.to.base.content.inventory.menu.ResearchMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.*
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.gamerules.GameRules
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class ResearchDeskBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(SEGMENT, ResearchDeskSegment.LEFT)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(FACING).add(SEGMENT).let {}

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val direction = context.horizontalDirection
        val state = defaultBlockState().setValue(FACING, direction)
        val pos1 = context.clickedPos.relative(direction.clockWise)
        if (!context.level.getBlockState(pos1).canBeReplaced(context) || !context.level.isUnobstructed(state, pos1, CollisionContext.empty())) {
            val pos2 = context.clickedPos.relative(direction.counterClockWise)
            if (!context.level.getBlockState(pos2).canBeReplaced(context) || !context.level.isUnobstructed(state, pos2, CollisionContext.empty())) {
                return null
            }
            return horizontalPlacement(context).setValue(SEGMENT, ResearchDeskSegment.RIGHT)
        }
        return horizontalPlacement(context).setValue(SEGMENT, ResearchDeskSegment.LEFT)
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)
        if (!level.isClientSide) {
            val direction = state.getValue(FACING)
            if (state.getValue(SEGMENT) == ResearchDeskSegment.LEFT) {
                level.setBlock(pos.relative(direction.counterClockWise), state.setValue(SEGMENT, ResearchDeskSegment.RIGHT), 3)
            } else {
                level.setBlock(pos.relative(direction.clockWise), state.setValue(SEGMENT, ResearchDeskSegment.LEFT), 3)
            }
            level.updateNeighborsAt(pos, Blocks.AIR)
            state.updateNeighbourShapes(level, pos, 3)
        }
    }

    override fun updateShape(state: BlockState, level: LevelReader, scheduledTickAccess: ScheduledTickAccess, pos: BlockPos, direction: Direction, neighborPos: BlockPos, neighborState: BlockState, random: RandomSource): BlockState {
        if (direction.axis == Direction.Axis.Y) return state
        val current = state.getValue(FACING)
        when (state.getValue(SEGMENT)) {
            ResearchDeskSegment.LEFT -> if (current == direction.clockWise && !isSegment(neighborState, current, ResearchDeskSegment.RIGHT)) {
                if (level is LevelAccessor) {
                    level.destroyBlock(pos, false)
                }
                return state
            }
            ResearchDeskSegment.RIGHT -> if (current == direction.counterClockWise && !isSegment(neighborState, current, ResearchDeskSegment.LEFT)) {
                if (level is LevelAccessor) {
                    level.destroyBlock(pos, false)
                }
                return state
            }
        }
        return state
    }

    override fun playerDestroy(level: Level, player: Player, pos: BlockPos, state: BlockState, blockEntity: BlockEntity?, tool: ItemStack) {
        super.playerDestroy(level, player, pos, state.setValue(SEGMENT, ResearchDeskSegment.RIGHT), blockEntity, tool)

        val facing = state.getValue(FACING)
        val segment = state.getValue(SEGMENT)
        val direction = if (segment == ResearchDeskSegment.LEFT) facing.counterClockWise else facing.clockWise

        if (level is ServerLevel) {
            getDrops(state.setValue(SEGMENT, ResearchDeskSegment.LEFT), level, pos, blockEntity, player, tool).forEach { stack ->
                val d0 = EntityType.ITEM.height.toDouble() / 2.0
                val d1 = pos.x.toDouble() + 0.5 + direction.stepX.times(0.5) + Mth.nextDouble(level.random, -0.25, 0.25)
                val d2 = pos.y.toDouble() + 0.5 + direction.stepY.times(0.5) + Mth.nextDouble(level.random, -0.25, 0.25) - d0
                val d3 = pos.z.toDouble() + 0.5 + direction.stepZ.times(0.5) + Mth.nextDouble(level.random, -0.25, 0.25)
                if (!stack.isEmpty && level.gameRules.get(GameRules.BLOCK_DROPS)) {
                    val item = ItemEntity(level, d1, d2, d3, stack)
                    item.setDefaultPickUpDelay()
                    level.addFreshEntity(item)
                }
            }
            state.spawnAfterBreak(level, pos, tool, true)
        }
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

    private fun isSegment(state: BlockState, facing: Direction, segment: ResearchDeskSegment) = state.block === this && state.getValue(FACING) == facing && state.getValue(Companion.SEGMENT) == segment

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = state.getValue(SEGMENT).shapes[state.getValue(FACING)]!!

    public override fun codec() = CODEC

    companion object {
        val SEGMENT = EnumProperty.create("segment", ResearchDeskSegment::class.java)
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