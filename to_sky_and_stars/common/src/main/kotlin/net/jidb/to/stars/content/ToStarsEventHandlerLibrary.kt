package net.jidb.to.stars.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.EventHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.event.handler.AdvancementRaceEventHandler
import net.jidb.to.stars.event.handler.EnergyItemStorageEventHandler

object ToStarsEventHandlerLibrary : EventHandlerLibrary(ToStarsMod.modid) {

    val advancement_race by this(AdvancementRaceEventHandler(), ToBaseMod.events::advancement_grant_post)
        .deferBuild(ToBaseMod::onInitialised)
    val energy_item_storage by this(EnergyItemStorageEventHandler(), ToBaseMod.events::modify_default_components)
        .deferBuild(ToBaseMod::onInitialised)

}
