package net.jidb.to.stars.content

import net.jidb.to.base.library.TagLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

object ToStarsItemTagLibrary : TagLibrary<Item>(ToStarsMod.modid) {

    override val registryKey = Registries.ITEM

    val enriched_uranium by this()

}
