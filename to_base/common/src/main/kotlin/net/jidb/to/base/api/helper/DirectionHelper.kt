package net.jidb.to.base.api.helper

import net.minecraft.core.Direction

/**
 * A helper object that provides [Direction]-related utilities.
 *
 * @since 0.8.0
 */
object DirectionHelper {

    /**
     * A baked list of perpendicular directions, returned by [getPerpendicularDirections].
     *
     * @since 0.8.0
     */
    private val perps = mapOf(
        Direction.UP to listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST),
        Direction.DOWN to listOf(Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST),
        Direction.NORTH to listOf(Direction.UP, Direction.WEST, Direction.DOWN, Direction.EAST),
        Direction.EAST to listOf(Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH),
        Direction.SOUTH to listOf(Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST),
        Direction.WEST to listOf(Direction.UP, Direction.SOUTH, Direction.DOWN, Direction.NORTH)
    )

    /**
     * Gets a list of four perpendicular directions to the given direction.
     * These are all four directions not on the given [Direction.Axis].
     *
     * @param direction The [Direction] to get other perpendicular [Direction] objects from.
     * @return A [List] of [Direction] that are perpendicular to the specified [Direction].
     * @since 0.8.0
     */
    fun getPerpendicularDirections(direction: Direction) = perps[direction]!!

    /**
     * Gets a list of four perpendicular directions to the receiver direction.
     * These are all four directions not on this [Direction.Axis].
     *
     * @return A [List] of [Direction] that are perpendicular to the specified [Direction].
     * @since 0.8.0
     */
    val Direction.perpendiculars get() = getPerpendicularDirections(this)

}
