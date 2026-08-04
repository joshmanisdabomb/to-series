package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ItemTintLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.item.tint.BatteryItemTint

/**
 * [ItemTintLibrary] implementation holding where the colour of each of this mod's tinted items is read from.
 */
object ToStarsItemTintsLibrary : ItemTintLibrary(ToStarsMod.modid) {

    /**
     * Colours a battery's overlay by how much charge it is holding.
     */
    val battery by this(BatteryItemTint.codec)

}
