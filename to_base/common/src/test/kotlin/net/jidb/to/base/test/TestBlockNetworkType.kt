package net.jidb.to.base.test

import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

/**
 * A [BlockNetworkType] that classifies a position from a layout the test hands it rather than from the block that is there.
 *
 * A real type reads the state, which means a test of one needs the block registries; the networking itself never looks at a state beyond caching it, so a test of the networking does not.
 * The layout is read on every call rather than copied, so a test can change what is where between ticks and watch a network split or join.
 *
 * @param classify What a position is to this network, given the direction it is being reached from, or `null` where it is not part of one.
 * @since 0.8.0
 */
class TestBlockNetworkType(private val classify: (pos: BlockPos, from: Direction?) -> BlockNetworkPositionType?) : BlockNetworkType() {

    /**
     * Every network this type has been ticked with, in the order the ticks happened.
     *
     * @since 0.8.0
     */
    val ticked = mutableListOf<BlockNetwork>()

    /**
     * Creates a type that classifies a position by looking it up in a layout, ignoring the direction it is reached from.
     *
     * @param layout What each position is to this network, which positions absent from it are not part of.
     * @since 0.8.0
     */
    constructor(layout: Map<BlockPos, BlockNetworkPositionType>) : this({ pos, _ -> layout[pos] })

    override fun getPositionType(level: LevelReader, pos: BlockPos, state: BlockState, from: Direction?) = classify(pos, from)

    override fun tick(level: ServerLevel, network: BlockNetwork) {
        ticked.add(network)
    }

}
