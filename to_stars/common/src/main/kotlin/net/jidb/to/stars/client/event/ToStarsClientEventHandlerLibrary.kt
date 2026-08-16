package net.jidb.to.stars.client.event

import net.jidb.to.base.client.ToBaseClientMod
import net.jidb.to.base.pub.library.EventHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.event.handler.RotorMovementEventHandler

object ToStarsClientEventHandlerLibrary : EventHandlerLibrary(ToStarsMod.modid) {

    val rotor_blades_movement by this(RotorMovementEventHandler(), ToBaseClientMod.events::client_entity_tick_pre)
        .deferBuild(ToBaseClientMod::onInitialised)

}
