package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.gametest.ToGameTest
import net.jidb.to.base.api.gametest.ToGameTestHelper.clearBlock
import net.jidb.to.base.api.gametest.ToGameTestHelper.makeTestPlayer
import net.jidb.to.base.api.gametest.ToGameTestHelper.placeAsPlayer
import net.jidb.to.base.api.gametest.ToGameTestHelper.tryPlaceAsPlayer
import net.jidb.to.base.content.block.ResearchDeskBlock
import net.jidb.to.base.content.block.ResearchDeskBlock.ResearchDeskSegment
import net.jidb.to.base.pub.gametest.PlaceBreakDropGameTest
import net.jidb.to.base.pub.library.GameTestLibrary
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock

/**
 * [GameTestLibrary] implementation that registers the game tests of the content of To Lay the Foundations, and provides access to them in one place.
 *
 * @since 1.0.0
 */
object ToBaseGameTestLibrary : GameTestLibrary(ToBaseMod.modid) {

    /**
     * That a [ResearchDeskBlock] placed from one item lays itself out across two positions, that breaking either of them takes the other with it, and that the one desk it gives back is dropped from between the two rather than from the half that was broken.
     *
     * @since 1.0.0
     */
    val research_desk by this { ToGameTest(run = PlaceBreakDropGameTest({ ToBaseMod.content.blocks.research_desk }, segments = 2)) }

    /**
     * The same of a [ResearchDeskBlock] broken from its other half, since a desk drops from between its two halves whichever of them was the one that was hit.
     *
     * @since 1.0.0
     */
    val research_desk_far_half by this { ToGameTest(run = PlaceBreakDropGameTest({ ToBaseMod.content.blocks.research_desk }, segments = 2, breakAt = { it.last() })) }

    /**
     * Which way round a [ResearchDeskBlock] lays itself out, which is decided by which side of the position clicked has room for the other half.
     *
     * A desk prefers the player's right: given room there, what was clicked becomes its left half and the other half goes to the right of it.
     * Only where the right is taken does it fall back to the left, in which case what was clicked is its right half instead.
     * With neither side free there is nowhere for the other half to go at all, and nothing is placed.
     *
     * Left and right are the player's, and the desk faces back towards them, so the half named by [ResearchDeskBlock.segment] is the half as the player sees it.
     *
     * @since 1.0.0
     */
    val research_desk_handedness by this { ToGameTest { helper ->
        val block = ToBaseMod.content.blocks.research_desk
        val origin = BlockPos(2, 1, 2)
        //Everything is worked out in the positions of the scene rather than the level, so that the rule holds however the scene is turned.
        val looking = Direction.NORTH
        val right = origin.relative(looking.clockWise)
        val left = origin.relative(looking.counterClockWise)

        val player = helper.makeTestPlayer(looking)

        helper.placeAsPlayer(player, ItemStack(block), origin)
        helper.assertBlockProperty(origin, HorizontalDirectionalBlock.FACING, helper.getAbsoluteDirection(looking.opposite))
        helper.assertBlockProperty(origin, ResearchDeskBlock.segment, ResearchDeskSegment.LEFT)
        helper.assertBlockProperty(right, ResearchDeskBlock.segment, ResearchDeskSegment.RIGHT)
        helper.clearBlock(block)

        helper.setBlock(right, Blocks.STONE)
        helper.placeAsPlayer(player, ItemStack(block), origin)
        helper.assertBlockProperty(origin, HorizontalDirectionalBlock.FACING, helper.getAbsoluteDirection(looking.opposite))
        helper.assertBlockProperty(origin, ResearchDeskBlock.segment, ResearchDeskSegment.RIGHT)
        helper.assertBlockProperty(left, ResearchDeskBlock.segment, ResearchDeskSegment.LEFT)
        helper.clearBlock(block)

        helper.setBlock(left, Blocks.STONE)
        val result = helper.tryPlaceAsPlayer(player, ItemStack(block), origin)
        helper.assertFalse(result.consumesAction(), "A desk was placed at $origin with neither side free: the placement came back as $result.")
        helper.assertBlockNotPresent(block, origin)

        helper.succeed()
    } }

}
