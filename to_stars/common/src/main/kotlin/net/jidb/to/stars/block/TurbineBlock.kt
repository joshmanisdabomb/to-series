package net.jidb.to.stars.block

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.api.helper.BlockHelper.horizontalPlayerPlacement
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.EmptyToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.info.MachineTier
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT
import net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED
import kotlin.jvm.optionals.getOrNull

class TurbineBlock(val machine: MachineTier, properties: Properties) : Block(properties), ToEnergyWorldlyProvider {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH)
            .setValue(LIT, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(HORIZONTAL_FACING, LIT).let {}

    override fun getStateForPlacement(context: BlockPlaceContext) = horizontalPlayerPlacement(context)

    override fun updateShape(state: BlockState, level: LevelReader, ticks: ScheduledTickAccess, pos: BlockPos, directionToNeighbour: Direction, neighbourPos: BlockPos, neighbourState: BlockState, random: RandomSource): BlockState {
        val facing = state.getValue(HORIZONTAL_FACING)
        if (directionToNeighbour != facing) {
            return state
        }
        val lit = state.getValue(LIT)
        val rotor = level.getBlockState(pos.relative(facing))
        if (rotor.block is RotorBlock) {
            val powered = rotor.getValue(POWERED)
            if (powered != lit) {
                return state.cycle(LIT)
            }
            return state
        } else {
            return state.setValue(LIT, false)
        }
    }

    override fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?): ToEnergyTransferContext? {
        val facing = level.getBlockState(pos).getValue(HORIZONTAL_FACING)
        if (side == null || side == facing.opposite) {
            val rotor = level.getBlockEntity(pos.relative(facing), ToStarsMod.blockEntities.rotor_blades).getOrNull()
            return rotor?.energy[machine] ?: EmptyToEnergyTransferContext
        }
        return null
    }

    override fun codec() = codec

    companion object {

        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                MachineTier.codec.fieldOf("machine").forGetter(TurbineBlock::machine),
                propertiesCodec()
            )
                .apply(it, ::TurbineBlock)
        }

    }

}
