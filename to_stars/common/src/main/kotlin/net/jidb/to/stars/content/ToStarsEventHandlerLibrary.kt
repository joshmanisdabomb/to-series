package net.jidb.to.stars.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.EventHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.event.handler.AdvancementRaceEventHandler
import net.jidb.to.stars.event.handler.EnergyItemStorageEventHandler
import net.jidb.to.stars.event.handler.MagneticIronEventHandler

/**
 * [EventHandlerLibrary] implementation holding this mod's handlers for the base mod's cross-loader events.
 *
 * Each is built only once the base mod has come up, since the events they are bound to are declared there.
 */
object ToStarsEventHandlerLibrary : EventHandlerLibrary(ToStarsMod.modid) {

    /**
     * Records which player was first on the server to earn an advancement.
     */
    val advancement_race by this(AdvancementRaceEventHandler(), ToBaseMod.events::advancement_grant_post)
        .deferBuild(ToBaseMod::onInitialised)

    /**
     * Gives the vanilla items that this mod lets store energy the component they store it in.
     */
    val energy_item_storage by this(EnergyItemStorageEventHandler(), ToBaseMod.events::modify_default_components)
        .deferBuild(ToBaseMod::onInitialised)

    /**
     * Handles magnetic iron being used on a block.
     */
    val magnetic_iron by this(MagneticIronEventHandler(), ToBaseMod.events::use_item_on_block)
        .deferBuild(ToBaseMod::onInitialised)

}
