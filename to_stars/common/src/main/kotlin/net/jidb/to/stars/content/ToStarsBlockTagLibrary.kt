package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.TagLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

/**
 * [TagLibrary] implementation holding the block tags of this mod.
 */
object ToStarsBlockTagLibrary : TagLibrary<Block>(ToStarsMod.modid) {

    override val registryKey = Registries.BLOCK

    /**
     * Blocks a nuclear blast passes straight through without being weakened at all.
     */
    val nuke_passthrough by this()

    /**
     * Blocks that stop a nuclear blast, whatever their blast resistance would otherwise allow.
     */
    val nuke_shielding by this()

    /**
     * Blocks a nuclear blast cannot destroy.
     */
    val nuke_immune by this()

}
