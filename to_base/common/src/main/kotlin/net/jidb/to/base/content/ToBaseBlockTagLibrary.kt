package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.library.TagLibrary
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

object ToBaseBlockTagLibrary : TagLibrary<Block>(ToBaseMod.modid) {

    override val registryKey = Registries.BLOCK

    val shears_efficient by this(/* TODO functionality */)

}
