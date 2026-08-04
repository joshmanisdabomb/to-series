package net.jidb.to.base.pub.tags

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.TagLibrary
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

/**
 * [TagLibrary] implementation that declares the block tags of To Lay the Foundations, and provides access to them in one place.
 *
 * @since 0.3.0
 */
object ToBaseBlockTagLibrary : TagLibrary<Block>(ToBaseMod.modid) {

    override val registryKey = Registries.BLOCK

    /**
     * Blocks that shears break faster than another tool would.
     *
     * @since 0.3.0
     */
    val shears_efficient by this() //TODO functionality

}
