package net.jidb.to.stars.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ColorRGBA
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.ColoredFallingBlock
import net.minecraft.world.level.block.state.BlockState

/**
 * What a nuclear explosion leaves behind, which piles up where it lands and settles over time rather than falling the moment it is unsupported.
 *
 * Unlike ordinary falling sand it does not fall on being placed by the world or on a neighbour changing, only when it is randomly ticked or when a player places it, so that a blast leaves a mound rather than a column.
 *
 * @param color The colour of the falling block's dust.
 * @param properties The block's own properties.
 */
class NuclearWasteBlock(color: ColorRGBA, properties: Properties) : ColoredFallingBlock(color, properties) {

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) = Unit

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        if (placer is Player && level is ServerLevel) {
            fall(state, level, pos, RandomSource.create())
        }
    }

    override fun updateShape(state: BlockState, level: LevelReader, scheduledTickAccess: ScheduledTickAccess, pos: BlockPos, direction: Direction, neighborPos: BlockPos, neighborState: BlockState, random: RandomSource) = state

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        fall(state, level, pos, random)
    }

    /**
     * Lets the waste fall if there is nothing under it, which is what the ordinary falling block tick would have done.
     *
     * @param state The state of the block.
     * @param level The level it is in.
     * @param pos The position of the block.
     * @param random The randomness the fall is decided by.
     */
    fun fall(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        super.tick(state, level, pos, random)
    }

}
