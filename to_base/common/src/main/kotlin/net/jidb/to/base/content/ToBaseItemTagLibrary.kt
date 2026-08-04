package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.TagLibrary
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

/**
 * [TagLibrary] implementation that declares the item tags of To Lay the Foundations, and provides access to them in one place.
 *
 * @since 0.1.0
 */
object ToBaseItemTagLibrary : TagLibrary<Item>(ToBaseMod.modid) {

    override val registryKey = Registries.ITEM

    /**
     * Items that unlock a wiki article when placed on a research desk.
     *
     * @since 0.1.0
     */
    val research_desk_unlock by this()

}
