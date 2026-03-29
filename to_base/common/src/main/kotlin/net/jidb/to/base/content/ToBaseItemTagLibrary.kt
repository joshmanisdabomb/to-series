package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.library.TagLibrary
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

object ToBaseItemTagLibrary : TagLibrary<Item>(ToBaseMod.modid) {

    override val registryKey = Registries.ITEM

    val research_desk_unlock by this()

}