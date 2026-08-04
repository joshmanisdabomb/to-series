package net.jidb.to.base.test

import net.jidb.to.base.api.block.network.BlockNetwork
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.api.block.network.BlockNetworkType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import java.util.UUID

/**
 * Builds the layout a [TestBlockNetworkType] reads from rows of text, so that the shape of a network is legible in the test that uses it.
 *
 * Each row is one step in Z and each character one step in X, all at Y 0, which makes the top left of the diagram the origin:
 * ```
 * NetworkLayout.of(
 *     "O#O",
 *     ".#.",
 * )
 * ```
 *
 * @since 0.8.0
 */
object NetworkLayout {

    /**
     * The character standing for a position the network runs through.
     *
     * @since 0.8.0
     */
    const val PATH = '#'

    /**
     * The character standing for a position the network ends at.
     *
     * @since 0.8.0
     */
    const val NODE = 'O'

    /**
     * Reads rows of text into the positions they describe.
     *
     * @param rows The rows of the diagram, each of [PATH], [NODE] and any other character for a position that is not part of the network.
     * @return What each position named in the diagram is to the network.
     * @throws IllegalArgumentException If a row is a different length to the first.
     * @since 0.8.0
     */
    fun of(vararg rows: String): MutableMap<BlockPos, BlockNetworkPositionType> {
        val layout = mutableMapOf<BlockPos, BlockNetworkPositionType>()
        for ((z, row) in rows.withIndex()) {
            require(row.length == rows[0].length) { "Row $z is ${row.length} long, but the first row is ${rows[0].length}." }
            for ((x, char) in row.withIndex()) {
                when (char) {
                    PATH -> layout[BlockPos(x, 0, z)] = BlockNetworkPositionType.PATH
                    NODE -> layout[BlockPos(x, 0, z)] = BlockNetworkPositionType.NODE
                    else -> Unit
                }
            }
        }
        return layout
    }

    /**
     * Builds a [BlockNetwork] covering a layout outright, without walking a level to find it.
     *
     * This is what a test of the network itself wants, since how a network is worked out from a level is [net.jidb.to.base.hooks.level.storage.BlockNetworkSavedData]'s job rather than the network's.
     *
     * @param type The kind of network being built.
     * @param layout What each position of the network is to it, as read by [of].
     * @param states The cached state of each position, which is empty where a test does not care what is there.
     * @return The network covering that layout, under an ID of its own.
     * @since 0.8.0
     */
    fun network(type: BlockNetworkType, layout: Map<BlockPos, BlockNetworkPositionType>, states: Map<BlockPos, BlockState> = emptyMap()) = BlockNetwork(
        UUID.randomUUID(),
        type,
        layout.filterValues { it == BlockNetworkPositionType.NODE }.keys,
        layout.filterValues { it == BlockNetworkPositionType.PATH }.keys,
        states,
    )

}
