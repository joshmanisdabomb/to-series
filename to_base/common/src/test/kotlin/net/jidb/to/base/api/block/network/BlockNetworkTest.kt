package net.jidb.to.base.api.block.network

import net.jidb.to.base.test.MinecraftBootstrap
import net.jidb.to.base.test.NetworkLayout
import net.jidb.to.base.test.TestBlockNetworkType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Tests the routing a [BlockNetwork] works out over the positions it covers.
 *
 * A network is a graph of [BlockPos] that never reads the states it holds beyond caching them, so what block is at a position is beside the point here and the fixtures use vanilla ones.
 *
 * @since 0.8.0
 */
@ExtendWith(MinecraftBootstrap.Extension::class)
class BlockNetworkTest {

    /**
     * The kind of network the fixtures are built under, which nothing here ticks.
     *
     * @since 0.8.0
     */
    private val type = TestBlockNetworkType(emptyMap())

    /**
     * A network with three nodes, where one of them is reachable from both of the others but they are not reachable from each other:
     * ```
     * O#O
     * #..
     * #..
     * O..
     * ```
     * The node at the corner is the only one with a path out of it in two directions.
     *
     * @since 0.8.0
     */
    private val branched = NetworkLayout.of(
        "O#O",
        "#..",
        "#..",
        "O..",
    )

    /**
     * The corner node of [branched], which both of the others hang off.
     *
     * @since 0.8.0
     */
    private val corner = BlockPos(0, 0, 0)

    /**
     * The node one path east of [corner] in [branched].
     *
     * @since 0.8.0
     */
    private val near = BlockPos(2, 0, 0)

    /**
     * The node two paths south of [corner] in [branched].
     *
     * @since 0.8.0
     */
    private val far = BlockPos(0, 0, 3)

    /**
     * Checks that the positions of a network are its nodes and its paths together.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("positions covers every node and path of the network")
    fun positionsCoversNodesAndPaths() {
        val network = NetworkLayout.network(type, branched)

        assertEquals(setOf(corner, near, far), network.nodes)
        assertEquals(setOf(BlockPos(1, 0, 0), BlockPos(0, 0, 1), BlockPos(0, 0, 2)), network.paths)
        assertEquals(network.nodes + network.paths, network.positions)
    }

    /**
     * Checks that a node reports the directions it joins the network in, and only those.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("connections names the sides a node has a path on")
    fun connectionsNameTheSidesWithAPath() {
        val network = NetworkLayout.network(type, branched)

        assertEquals(setOf(Direction.EAST, Direction.SOUTH), network.connections[corner])
        assertEquals(setOf(Direction.WEST), network.connections[near])
        assertEquals(setOf(Direction.NORTH), network.connections[far])
    }

    /**
     * Checks that paths are not asked about their connections, since only nodes are what a network runs to.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("connections is empty for a position that is not a node")
    fun connectionsSkipsPaths() {
        val network = NetworkLayout.network(type, branched)

        assertNull(network.connections[BlockPos(1, 0, 0)])
    }

    /**
     * Checks the shape of a route: the two nodes at its ends, the paths in between, and the direction it leaves and arrives by.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("a route runs from node to node through the paths between them")
    fun aRouteRunsBetweenTwoNodes() {
        val network = NetworkLayout.network(type, branched)

        val route = assertNotNull(network.getShortestPath(corner, far))
        assertEquals(corner, route.from)
        assertEquals(far, route.to)
        assertEquals(listOf(BlockPos(0, 0, 1), BlockPos(0, 0, 2)), route.paths)
        assertEquals(listOf(corner, BlockPos(0, 0, 1), BlockPos(0, 0, 2), far), route.path)
        assertEquals(Direction.SOUTH, route.outgoing)
        assertEquals(Direction.SOUTH, route.incoming)
    }

    /**
     * Checks that a node ends the walk rather than being walked through, which is what keeps two machines joined by one cable from joining everything behind each other.
     *
     * The node east of the corner can reach the corner, but not what hangs off the far side of it.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("a route stops at the first node it reaches")
    fun aRouteStopsAtTheFirstNode() {
        val network = NetworkLayout.network(type, branched)

        assertNotNull(network.getShortestPath(near, corner))
        assertNull(network.getShortestPath(near, far))
    }

    /**
     * Checks that where two nodes are joined twice over, the route between them is the shorter of the two.
     *
     * ```
     * O#O
     * #.#
     * ###
     * ```
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("a route takes the shorter way round a loop")
    fun aRouteTakesTheShorterWayRound() {
        val looped = NetworkLayout.of(
            "O#O",
            "#.#",
            "###",
        )
        val network = NetworkLayout.network(type, looped)

        val route = assertNotNull(network.getShortestPath(BlockPos(0, 0, 0), BlockPos(2, 0, 0)))
        assertEquals(listOf(BlockPos(1, 0, 0)), route.paths)
        assertEquals(Direction.EAST, route.outgoing)
    }

    /**
     * Checks that the routes out of a node are ordered by how many paths they run through, so that the nearest thing on the network is dealt with first.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("the routes of a node come back nearest first")
    fun routesComeBackNearestFirst() {
        val network = NetworkLayout.network(type, branched)

        assertEquals(listOf(near, far), network.getPathsByDistance(corner).map { it.to })
    }

    /**
     * Checks that asking a node for its routes on one side leaves out the ones that leave by another.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("the routes of a node can be narrowed to one side")
    fun routesCanBeNarrowedToOneSide() {
        val network = NetworkLayout.network(type, branched)

        assertEquals(listOf(far), network.getPathsByDistance(corner, Direction.SOUTH).map { it.to })
        assertEquals(listOf(near), network.getPathsByDistance(corner, Direction.EAST).map { it.to })
        assertEquals(emptyList(), network.getPathsByDistance(corner, Direction.NORTH))
    }

    /**
     * Checks that the states of a route line up one for one with its positions, which is what saves a caller reading the level again.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("a route carries the state of each position it runs through")
    fun aRouteCarriesTheStateOfEachPosition() {
        val states = branched.keys.associateWith { Blocks.STONE.defaultBlockState() }
        val network = NetworkLayout.network(type, branched, states)

        val route = assertNotNull(network.getShortestPath(corner, far))
        assertEquals(route.path.map { states[it] }, route.states)
    }

    /**
     * Checks that a position that is not on the network is not routed to.
     *
     * @since 0.8.0
     */
    @Test
    @DisplayName("there is no route to a node of another network")
    fun thereIsNoRouteOffTheNetwork() {
        val network = NetworkLayout.network(type, branched)

        assertNull(network.getShortestPath(corner, BlockPos(9, 0, 9)))
        assertNull(network.getShortestPath(BlockPos(9, 0, 9), corner))
    }

}
