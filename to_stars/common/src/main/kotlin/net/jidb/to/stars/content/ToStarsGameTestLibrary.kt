package net.jidb.to.stars.content

import net.jidb.to.base.api.gametest.ToGameTest
import net.jidb.to.base.pub.gametest.PlaceBreakDropGameTest
import net.jidb.to.base.pub.library.GameTestLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.AtomicBombBlock

/**
 * [GameTestLibrary] implementation that registers the game tests of To Sky and Stars, and provides access to them in one place.
 */
object ToStarsGameTestLibrary : GameTestLibrary(ToStarsMod.modid) {

    /**
     * That an [AtomicBombBlock] placed from one item lays itself out across three positions, that breaking any of them takes the other two with it, and that the one bomb it gives back is dropped from the middle segment rather than from an end.
     */
    val atomic_bomb by this { ToGameTest(run = PlaceBreakDropGameTest({ ToStarsMod.blocks.atomic_bomb }, segments = 3)) }

}
