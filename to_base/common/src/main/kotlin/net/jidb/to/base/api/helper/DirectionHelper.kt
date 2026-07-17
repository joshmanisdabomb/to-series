package net.jidb.to.base.api.helper

import net.minecraft.core.Direction

object DirectionHelper {

    private val perps = mapOf(
        Direction.UP to listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST),
        Direction.DOWN to listOf(Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST),
        Direction.NORTH to listOf(Direction.UP, Direction.WEST, Direction.DOWN, Direction.EAST),
        Direction.EAST to listOf(Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH),
        Direction.SOUTH to listOf(Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST),
        Direction.WEST to listOf(Direction.UP, Direction.SOUTH, Direction.DOWN, Direction.NORTH)
    )

    fun getPerpendicularDirections(direction: Direction) = perps[direction]!!
    val Direction.perpendiculars get() = getPerpendicularDirections(this)

}