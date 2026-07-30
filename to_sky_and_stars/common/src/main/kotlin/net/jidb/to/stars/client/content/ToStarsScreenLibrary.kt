package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ScreenLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.gui.screens.AtomicBombScreen
import net.jidb.to.stars.client.gui.screens.CentrifugeScreen
import net.jidb.to.stars.client.gui.screens.EnergyStorageScreen
import net.jidb.to.stars.client.gui.screens.HeatGeneratorScreen
import net.jidb.to.stars.content.ToStarsMenuLibrary

object ToStarsScreenLibrary : ScreenLibrary(ToStarsMod.modid) {

    val atomic_bomb by this { ScreenEntry({ ToStarsMenuLibrary.atomic_bomb }, ::AtomicBombScreen) }
    val energy_storage by this { ScreenEntry({ ToStarsMenuLibrary.energy_storage }, ::EnergyStorageScreen) }
    val solid_generator by this { ScreenEntry({ ToStarsMenuLibrary.solid_generator }, ::HeatGeneratorScreen) }
    val centrifuge by this { ScreenEntry({ ToStarsMenuLibrary.centrifuge }, ::CentrifugeScreen) }

}
