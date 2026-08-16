package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ItemTintLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.item.tint.BatteryItemTint

object ToStarsItemTintsLibrary : ItemTintLibrary(ToStarsMod.modid) {

    val battery by this(BatteryItemTint.codec)

}
