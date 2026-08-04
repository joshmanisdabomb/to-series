package net.jidb.to.base.pub.gametest

import net.jidb.to.base.api.gametest.ToGameTestHelper.assertPositionsInLine
import net.jidb.to.base.api.gametest.ToGameTestHelper.assertSingleItemDropAt
import net.jidb.to.base.api.gametest.ToGameTestHelper.breakAsPlayer
import net.jidb.to.base.api.gametest.ToGameTestHelper.getBlockPositions
import net.jidb.to.base.api.gametest.ToGameTestHelper.makeTestPlayer
import net.jidb.to.base.api.gametest.ToGameTestHelper.middleOf
import net.jidb.to.base.api.gametest.ToGameTestHelper.placeAsPlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block

/**
 * The whole life of a block that occupies more than one position: it is placed from a single item and lays itself out across several, breaking any one of those takes the rest with it, and the one item it gives back is dropped from the middle of where it stood.
 *
 * This is the shape every multi-block in the To series shares, so a mod that has one only has to name it rather than write the test:
 * ```kotlin
 * val example by this { ToGameTest(run = PlaceBreakDropGameTest({ ExampleMod.blocks.example }, segments = 3)) }
 * ```
 *
 * Nothing here knows which way round the block lays itself out or which of its positions is which.
 * The test places the block, reads back where it actually went, and judges it on that: the positions have to be a straight unbroken line of the expected length through the one that was clicked, they all have to go at once, and the single drop has to lie in the middle of the line rather than at whichever end was broken.
 * That makes it as true of a two-position block placed sideways as of a three-position one placed lengthways, at any rotation.
 *
 * @param block The block being tested, resolved when the test runs rather than when it is declared, since a library is not built yet at declaration time.
 * @param segments How many positions the block is expected to take up.
 * @param origin The position clicked to place it, relative to the scene. Defaults to one above the middle of the default five by four by five platform.
 * @param facing The direction the placing player looks in. Defaults to [Direction.NORTH].
 * @param breakAt Which of the positions the block went to is broken, chosen once they are known rather than named in advance, so that a test can break an end of the block without having to know which way round it laid itself out. Defaults to the one that was clicked.
 * @param item The item the block is placed from and expected to drop. Defaults to the block's own item.
 * @param tool What the player holds while breaking, which has to be good enough for a block that asks for a correct tool. Defaults to a netherite pickaxe.
 * @since 1.0.0
 */
class PlaceBreakDropGameTest(
    private val block: () -> Block,
    private val segments: Int,
    private val origin: BlockPos = BlockPos(2, 1, 2),
    private val facing: Direction = Direction.NORTH,
    private val breakAt: (placed: List<BlockPos>) -> BlockPos = { origin },
    private val item: () -> Item = { block().asItem() },
    private val tool: () -> ItemStack = { ItemStack(Items.NETHERITE_PICKAXE) },
) : (GameTestHelper) -> Unit {

    override fun invoke(helper: GameTestHelper) {
        val block = block()
        val item = item()

        val player = helper.makeTestPlayer(facing)
        helper.placeAsPlayer(player, ItemStack(item), origin)

        val placed = helper.getBlockPositions(block)
        helper.assertValueEqual(placed.size, segments, "the number of positions ${block.descriptionId} was placed across")
        helper.assertTrue(origin in placed, "$origin was clicked to place ${block.descriptionId} but holds none of it.")
        helper.assertPositionsInLine(placed)

        //Read before the block is broken, since afterwards there is nothing left to work it out from.
        val middle = middleOf(placed.first(), placed.last())
        val broken = breakAt(placed)

        helper.breakAsPlayer(player, broken, tool())

        helper.assertValueEqual(helper.getBlockPositions(block).size, 0, "the number of positions ${block.descriptionId} was left across after breaking $broken")
        helper.assertSingleItemDropAt(item, middle)

        helper.succeed()
    }

}
