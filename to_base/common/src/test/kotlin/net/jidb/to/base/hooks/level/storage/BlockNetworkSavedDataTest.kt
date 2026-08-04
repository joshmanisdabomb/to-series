package net.jidb.to.base.hooks.level.storage

import io.mockk.every
import io.mockk.mockk
import net.jidb.to.base.api.block.network.BlockNetworkPositionType
import net.jidb.to.base.test.MinecraftBootstrap
import net.jidb.to.base.test.NetworkLayout
import net.jidb.to.base.test.TestBlockNetworkType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Tests how [BlockNetworkSavedData] works a network out of a level, i.e. what a change ends up rebuilding and what shape it comes back as.
 *
 * The level is a stub that answers every position with the same state, and the kind of network reads a layout the test holds rather than that state, so changing what is where is a matter of editing the layout between ticks.
 *
 * @since 0.8.0
 */
@ExtendWith(MinecraftBootstrap.Extension::class)
class BlockNetworkSavedDataTest {

    /**
     * The state every position of the stub level reports, which nothing here looks at.
     *
     * @since 0.8.0
     */
    private val state = Blocks.STONE.defaultBlockState()

    /**
     * The level the networks are worked out in, which answers a state and nothing else.
     *
     * @since 0.8.0
     */
    private val level = mockk<ServerLevel>().also {
        every { it.getBlockState(any()) } returns state
    }

    /**
     * Builds the saved data of a level whose networks follow the given layout, having already settled once.
     *
     * @param layout What each position is to the network, which the returned type keeps reading, so that editing it changes the level.
     * @return The saved data and the kind of network its contents are of.
     * @since 0.8.0
     */
    private fun settled(layout: MutableMap<BlockPos, BlockNetworkPositionType>): Pair<BlockNetworkSavedData, TestBlockNetworkType> {
        val type = TestBlockNetworkType(layout)
        val data = BlockNetworkSavedData()
        data.notify(type, *layout.keys.toTypedArray())
        data.tick(level)
        return data to type
    }

    /**
     * Checks that a run of paths with something on each end comes back as one network, of the paths it runs through and the nodes it ends at.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("a change builds one network out of the paths it reaches")
    fun aChangeBuildsOneNetwork() {
        val (data, _) = settled(NetworkLayout.of("O###O"))

        val networks = assertNotNull(data[BlockPos(2, 0, 0)])
        assertEquals(1, networks.size)
        assertEquals(setOf(BlockPos(1, 0, 0), BlockPos(2, 0, 0), BlockPos(3, 0, 0)), networks[0].paths)
        assertEquals(setOf(BlockPos(0, 0, 0), BlockPos(4, 0, 0)), networks[0].nodes)
    }

    /**
     * Checks that the walk stops at a node rather than carrying on through it, so that two runs of path either side of one stay separate networks.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("a node ends the walk instead of joining what is behind it")
    fun aNodeEndsTheWalk() {
        val (data, _) = settled(NetworkLayout.of("#O#"))

        val left = assertNotNull(data[BlockPos(0, 0, 0)])
        val right = assertNotNull(data[BlockPos(2, 0, 0)])
        assertEquals(1, left.size)
        assertEquals(1, right.size)
        assertNotEqualIds(left[0].id, right[0].id)

        //The node in the middle belongs to both, since each of them ends at it.
        assertEquals(2, assertNotNull(data[BlockPos(1, 0, 0)]).size)
    }

    /**
     * Checks that a position with nothing on it is in no network at all.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("a position off the network belongs to nothing")
    fun aPositionOffTheNetworkBelongsToNothing() {
        val (data, _) = settled(NetworkLayout.of("O###O"))

        assertNull(data[BlockPos(0, 0, 9)])
    }

    /**
     * Checks that breaking a path in the middle of a network splits it into the two networks either side, rather than leaving the old one behind.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("breaking a path splits the network in two")
    fun breakingAPathSplitsTheNetwork() {
        val layout = NetworkLayout.of("O#####O")
        val (data, type) = settled(layout)

        val before = assertNotNull(data[BlockPos(1, 0, 0)])
        assertEquals(1, before.size)
        assertEquals(5, before[0].paths.size)

        val broken = BlockPos(3, 0, 0)
        layout.remove(broken)
        data.notifyWithNeighbors(type, broken)
        data.tick(level)

        assertNull(data[broken])
        val left = assertNotNull(data[BlockPos(1, 0, 0)])
        val right = assertNotNull(data[BlockPos(5, 0, 0)])
        assertEquals(setOf(BlockPos(1, 0, 0), BlockPos(2, 0, 0)), left[0].paths)
        assertEquals(setOf(BlockPos(4, 0, 0), BlockPos(5, 0, 0)), right[0].paths)
        assertNotEqualIds(left[0].id, right[0].id)
    }

    /**
     * Checks that filling the gap between two networks joins them into one, which is the same rebuild running the other way round.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("laying a path joins two networks into one")
    fun layingAPathJoinsTwoNetworks() {
        val layout = NetworkLayout.of("O#.#O")
        val (data, type) = settled(layout)

        assertNotEqualIds(assertNotNull(data[BlockPos(1, 0, 0)])[0].id, assertNotNull(data[BlockPos(3, 0, 0)])[0].id)

        val laid = BlockPos(2, 0, 0)
        layout[laid] = BlockNetworkPositionType.PATH
        data.notifyWithNeighbors(type, laid)
        data.tick(level)

        val joined = assertNotNull(data[laid])
        assertEquals(1, joined.size)
        assertEquals(setOf(BlockPos(1, 0, 0), laid, BlockPos(3, 0, 0)), joined[0].paths)
        assertEquals(setOf(BlockPos(0, 0, 0), BlockPos(4, 0, 0)), joined[0].nodes)
    }

    /**
     * Checks that a tick with nothing queued still ticks the networks that are there, since that is what a network does its work on.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("every network of the level is ticked")
    fun everyNetworkIsTicked() {
        val layout = NetworkLayout.of("#O#")
        val (data, type) = settled(layout)
        type.ticked.clear()

        data.tick(level)

        assertEquals(2, type.ticked.size)
    }

    /**
     * Checks that two networks are not the same one.
     *
     * @param first The ID of the first network.
     * @param second The ID of the second network.
     * @since 0.8.0
     */
    private fun assertNotEqualIds(first: UUID, second: UUID) {
        assertNotEquals(first, second, "Expected two separate networks, but both had the ID $first.")
    }

}
