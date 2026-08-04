package net.jidb.to.base.api.gametest

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.GameType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.abs

/**
 * Utility object holding the parts of a game test that are the same whichever block is being tested, as extensions of [GameTestHelper].
 *
 * Everything here works in the positions the test itself is written in, i.e. relative to the corner of the structure and turned with it, so a test using them survives being run at a rotation.
 *
 * @since 1.0.0
 */
object ToGameTestHelper {

    /**
     * How far above the scene a test player is put, in blocks, so that it neither obstructs a placement nor picks up what the test drops.
     *
     * @since 1.0.0
     */
    private const val PLAYER_HEIGHT = 8.0

    /**
     * Creates a survival mode player in the level, standing clear of the scene and facing a direction.
     *
     * Placing and breaking a block as a player is not the same as setting it, since only the player path runs the hooks a block uses to place or break the rest of itself, so a test of a multi-block wants this rather than [GameTestHelper.setBlock] and [GameTestHelper.destroyBlock].
     *
     * @param facing The direction the player looks in, which is what a block placed by it reads its own facing from.
     * @return The player, which stays in the level for the rest of the test.
     * @since 1.0.0
     */
    fun GameTestHelper.makeTestPlayer(facing: Direction): ServerPlayer {
        val player = makeMockServerPlayerInLevel()
        player.setGameMode(GameType.SURVIVAL)
        player.snapTo(absoluteVec(Vec3(0.0, relativeBounds.maxY + PLAYER_HEIGHT, 0.0)), getAbsoluteDirection(facing).toYRot(), 0.0F)
        return player
    }

    /**
     * Places a stack at a position as the given player would, i.e. running the hooks that a block only sees when a player is the one placing it.
     *
     * This is not [GameTestHelper.placeAt], which clicks the face of a position rather than filling it and so places one block further along than it is asked to.
     * The stack is put in the player's hand first, since what a placement reads and consumes is what is being held rather than what it was handed.
     *
     * @param player The player placing the stack, whose facing the placed block reads.
     * @param stack What is being placed.
     * @param pos Where it is being placed, relative to the scene.
     * @throws net.minecraft.gametest.framework.GameTestAssertException If nothing was placed.
     * @since 1.0.0
     */
    fun GameTestHelper.placeAsPlayer(player: ServerPlayer, stack: ItemStack, pos: BlockPos) {
        val result = tryPlaceAsPlayer(player, stack, pos)
        assertTrue(result.consumesAction(), "Nothing was placed at $pos: the placement came back as $result.")
    }

    /**
     * The same as [placeAsPlayer], but handing back what the placement came to rather than insisting it happened, which is what a test of when a block refuses to be placed wants.
     *
     * @param player The player placing the stack, whose facing the placed block reads.
     * @param stack What is being placed.
     * @param pos Where it is being placed, relative to the scene.
     * @return What the placement came to, which is an action-consuming result only where something was actually placed.
     * @since 1.0.0
     */
    fun GameTestHelper.tryPlaceAsPlayer(player: ServerPlayer, stack: ItemStack, pos: BlockPos): InteractionResult {
        player.setItemInHand(InteractionHand.MAIN_HAND, stack)
        val absolute = absolutePos(pos)
        //Clicking the position itself rather than a face of it, which is what a player clicking through air does, so that the block lands exactly where it was asked for.
        val hit = BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false)
        return stack.useOn(UseOnContext(player, InteractionHand.MAIN_HAND, hit))
    }

    /**
     * Takes every position of a block out of the scene without breaking it, so that a test can set the same thing up again from scratch.
     *
     * Nothing is dropped and no hook is run, since this is clearing the scene rather than testing a removal.
     *
     * @param block The block being cleared.
     * @throws net.minecraft.gametest.framework.GameTestAssertException If any of it is left afterwards.
     * @since 1.0.0
     */
    fun GameTestHelper.clearBlock(block: Block) {
        getBlockPositions(block).forEach { setBlock(it, Blocks.AIR) }
        assertValueEqual(getBlockPositions(block).size, 0, "the number of positions of ${block.descriptionId} left after clearing it")
    }

    /**
     * Breaks the block at a position as the given player would, i.e. running the hooks that a block only sees when a player is the one breaking it.
     *
     * @param player The player breaking the block.
     * @param pos The position being broken, relative to the scene.
     * @param tool What the player holds while breaking, which decides whether a block asking for a correct tool drops anything. Defaults to nothing.
     * @throws net.minecraft.gametest.framework.GameTestAssertException If there was nothing there to break.
     * @since 1.0.0
     */
    fun GameTestHelper.breakAsPlayer(player: ServerPlayer, pos: BlockPos, tool: ItemStack = ItemStack.EMPTY) {
        player.setItemInHand(InteractionHand.MAIN_HAND, tool)
        assertTrue(player.gameMode.destroyBlock(absolutePos(pos)), "Nothing was broken at $pos.")
    }

    /**
     * Every position of the scene currently holding a block, in the order a position is reached reading along x, then y, then z.
     *
     * @param block The block being looked for.
     * @return The positions holding it, relative to the scene.
     * @since 1.0.0
     */
    fun GameTestHelper.getBlockPositions(block: Block): List<BlockPos> {
        val positions = mutableListOf<BlockPos>()
        forEveryBlockInStructure { pos ->
            if (getBlockState(pos).`is`(block)) {
                positions.add(pos.immutable())
            }
        }
        return positions.sortedWith(compareBy({ it.x }, { it.y }, { it.z }))
    }

    /**
     * Asserts that a run of positions is a straight unbroken line, i.e. that they differ along one axis only and that none of the line is missing.
     * A multi-block that has placed itself correctly reads as one of these, whichever way round it was placed.
     *
     * @param positions The positions being checked, in the order [getBlockPositions] returns them.
     * @throws net.minecraft.gametest.framework.GameTestAssertException If the positions are not a line, or the line has a gap in it.
     * @since 1.0.0
     */
    fun GameTestHelper.assertPositionsInLine(positions: List<BlockPos>) {
        assertTrue(positions.isNotEmpty(), "There are no positions to be in a line.")
        val first = positions.first()
        val last = positions.last()
        val axes = Direction.Axis.VALUES.filter { first.get(it) != last.get(it) }
        assertTrue(axes.size <= 1, "$positions do not lie along one axis.")
        val axis = axes.firstOrNull() ?: return
        assertValueEqual(last.get(axis) - first.get(axis) + 1, positions.size, "the length of the line $positions")
    }

    /**
     * Asserts that the scene holds exactly one dropped item of the given kind, and that it lies within a tolerance of a position.
     *
     * @param item The item expected to have dropped.
     * @param at Where it is expected to lie, relative to the scene.
     * @param tolerance How far off that position each axis may be. The vertical is looser than the horizontal because a drop is spawned half an item's height below the point it is thrown from.
     * @throws net.minecraft.gametest.framework.GameTestAssertException If there is not exactly one such drop, or it lies outside the tolerance.
     * @since 1.0.0
     */
    fun GameTestHelper.assertSingleItemDropAt(item: Item, at: Vec3, tolerance: Vec3 = Vec3(0.3, 0.4, 0.3)) {
        val drops = getEntities(EntityTypes.ITEM).filter { it.item.`is`(item) }
        assertValueEqual(drops.size, 1, "the number of drops of ${item.descriptionId}")

        val position = relativeVec(drops.single().position())
        val offset = position.subtract(at)
        assertTrue(
            abs(offset.x) <= tolerance.x && abs(offset.y) <= tolerance.y && abs(offset.z) <= tolerance.z,
            "The drop lies at $position rather than within $tolerance of $at."
        )
    }

    /**
     * The point halfway between the centres of two positions, which for a run of blocks placed in a line is the middle of the whole thing.
     *
     * @param first One end of the run.
     * @param last The other end of it.
     * @return The point in the middle, relative to the scene.
     * @since 1.0.0
     */
    fun middleOf(first: BlockPos, last: BlockPos): Vec3 = Vec3.atCenterOf(first).add(Vec3.atCenterOf(last)).scale(0.5)

}
