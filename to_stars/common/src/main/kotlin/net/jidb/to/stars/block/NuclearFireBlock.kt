package net.jidb.to.stars.block

import net.jidb.to.base.mixin.FireBlockAccessor
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock
import net.minecraft.world.level.block.state.BlockState

/**
 * The fire a nuclear explosion burns with, which spreads much further than an ordinary fire and leaves nuclear waste behind it.
 *
 * @param properties The block's own properties.
 */
class NuclearFireBlock(properties: Properties) : FireBlock(properties) {

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        for (direction in Direction.entries) {
            val pos2 = pos.relative(direction)
            val state2 = level.getBlockState(pos2)
            if (faceSupports(state2, level, pos2, direction.opposite)) {
                return true
            }
        }
        return false
    }

    override fun canBurn(state: BlockState) = true

    override fun updateShape(state: BlockState, level: LevelReader, scheduledTickAccess: ScheduledTickAccess, pos: BlockPos, direction: Direction, neighborPos: BlockPos, neighborState: BlockState, random: RandomSource): BlockState {
        if (!this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState()
        }
        return getStateForPlacement(level, pos).setValue(AGE, state.getValue(AGE))
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) = Unit

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (level.canSpreadFireAround(pos)) {
            spread(level, pos, state, random)
        }
    }

    /**
     * Spreads the fire through the blocks around it, leaving nuclear waste where it burns, and removes it once it has nothing left to burn or has aged out.
     *
     * @param level The level the fire is in.
     * @param pos The position of the fire.
     * @param state The state of the fire.
     * @param random The randomness the spread is decided by.
     */
    private fun spread(level: ServerLevel, pos: BlockPos, state: BlockState, random: RandomSource) {
        if (!canSurvive(state, level, pos) || state.getValue(AGE) >= MAX_AGE) {
            level.removeBlock(pos, false)
            return
        }
        val mpos = BlockPos.MutableBlockPos()
        val waste = ToStarsMod.blocks.nuclear_waste.defaultBlockState()

        for (i in -2..2) {
            for (j in -3..3) {
                for (k in -2..2) {
                    if (i == 0 && j == 0 && k == 0) continue
                    mpos.set(pos).move(i, j, k)

                    val target = level.getBlockState(mpos)
                    val rand = random.nextDouble()
                    if (target.isAir) {
                        if (rand < 0.004) {
                            level.setBlock(mpos, waste, 3)
                            ToStarsMod.blocks.nuclear_waste.fall(waste, level, mpos, random)
                        } else if (rand < 0.05) {
                            val fire = getStateForPlacement(level, mpos)
                            if (fire.`is`(this)) {
                                level.setBlock(mpos, fire.setValue(AGE, state.getValue(AGE).plus(1 + random.nextInt(3)).coerceAtMost(MAX_AGE)), 3)
                            }
                        }
                    } else {
                        if (rand < 0.03 && !target.`is`(ToStarsMod.blocks.nuclear_waste) && target.block.explosionResistance < 100f) {
                            level.destroyBlock(mpos, false)
                            level.setBlock(mpos, waste, 3)
                            ToStarsMod.blocks.nuclear_waste.fall(waste, level, mpos, random)
                        }
                    }
                }
            }
        }
        level.setBlock(pos, state.setValue(AGE, state.getValue(AGE).plus(1 + random.nextInt(5)).coerceAtMost(MAX_AGE)), 3)
    }

    public override fun getStateForPlacement(level: BlockGetter, pos: BlockPos): BlockState {
        var fire = defaultBlockState()
        for (direction in Direction.entries) {
            val pos2 = pos.relative(direction)
            val state = level.getBlockState(pos2)
            if (faceSupports(state, level, pos2, direction.opposite)) {
                if (direction == Direction.DOWN) {
                    return defaultBlockState()
                }
                fire = fire.setValue(PROPERTY_BY_DIRECTION[direction]!!, true)
            }
        }
        return fire
    }

    /**
     * Whether a block's face can hold this fire up, which either a solid face or anything ordinary fire would catch on does.
     *
     * @param state The state of the block.
     * @param level The level the block is in.
     * @param pos The position of the block.
     * @param direction The side the fire would sit against.
     * @return Returns `true` if the face can hold the fire up, otherwise `false`.
     */
    private fun faceSupports(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Boolean {
        if (state.isFaceSturdy(level, pos, direction.opposite)) {
            return true
        } else if ((Blocks.FIRE as FireBlockAccessor).`to_base$getIgniteOdds`().getInt(state.block) > 0) {
            return true
        }
        return false
    }

}
