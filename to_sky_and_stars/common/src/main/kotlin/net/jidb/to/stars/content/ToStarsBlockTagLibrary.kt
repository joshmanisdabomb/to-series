package net.jidb.to.stars.content

import net.jidb.to.base.library.TagLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

object ToStarsBlockTagLibrary : TagLibrary<Block>(ToStarsMod.modid) {

    override val registryKey = Registries.BLOCK

    val nuke_passthrough by this()
    val nuke_shielding by this()
    val nuke_immune by this()

}