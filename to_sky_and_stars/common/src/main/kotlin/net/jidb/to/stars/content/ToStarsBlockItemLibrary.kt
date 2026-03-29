package net.jidb.to.stars.content

import net.jidb.to.base.library.BlockItemLibrary
import net.jidb.to.stars.ToStarsMod

object ToStarsBlockItemLibrary : BlockItemLibrary(ToStarsMod.MOD_ID, ToStarsMod.blocks) {

    val nuclear_fire by this { null }.evaluateValue()

}