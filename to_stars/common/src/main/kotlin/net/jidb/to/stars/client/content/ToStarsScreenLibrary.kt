package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ScreenLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.gui.screens.AtomicBombScreen
import net.jidb.to.stars.client.gui.screens.CentrifugeScreen
import net.jidb.to.stars.client.gui.screens.EnergyStorageScreen
import net.jidb.to.stars.client.gui.screens.HeatGeneratorScreen
import net.jidb.to.stars.content.ToStarsMenuLibrary

/**
 * [ScreenLibrary] implementation holding the screen each of this mod's menus is drawn as.
 */
object ToStarsScreenLibrary : ScreenLibrary(ToStarsMod.modid) {

    /**
     * The screen an atomic bomb is armed through.
     */
    val atomic_bomb by this { ScreenEntry({ ToStarsMenuLibrary.atomic_bomb }, ::AtomicBombScreen) }

    /**
     * The screen a power bank is opened into.
     */
    val energy_storage by this { ScreenEntry({ ToStarsMenuLibrary.energy_storage }, ::EnergyStorageScreen) }

    /**
     * The screen a solid generator is opened into.
     */
    val solid_generator by this { ScreenEntry({ ToStarsMenuLibrary.solid_generator }, ::HeatGeneratorScreen) }

    /**
     * The screen a centrifuge is opened into.
     */
    val centrifuge by this { ScreenEntry({ ToStarsMenuLibrary.centrifuge }, ::CentrifugeScreen) }

}
