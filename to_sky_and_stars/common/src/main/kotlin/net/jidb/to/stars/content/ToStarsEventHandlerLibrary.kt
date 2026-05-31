package net.jidb.to.stars.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.library.EventHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.event.handler.AdvancementRaceEventHandler

object ToStarsEventHandlerLibrary : EventHandlerLibrary(ToStarsMod.modid) {

    val advancement_race by this(AdvancementRaceEventHandler(), ToBaseMod.events::advancement_grant_post)
        .deferBuild(ToBaseMod::onInitialised)

}
