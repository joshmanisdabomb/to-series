package net.jidb.to.stars.client.content

import net.jidb.to.base.client.library.ScreenLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.screens.AtomicBombScreen
import net.jidb.to.stars.content.ToStarsMenuLibrary

object ToStarsScreenLibrary : ScreenLibrary(ToStarsMod.modid) {

    val atomic_bomb by this { ScreenEntry({ ToStarsMenuLibrary.atomic_bomb }, ::AtomicBombScreen) }

}
