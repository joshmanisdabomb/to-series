package net.jidb.to.stars.client.event

import net.jidb.to.base.client.ToBaseClientMod
import net.jidb.to.base.pub.library.EventHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.event.handler.RotorMovementEventHandler

/**
 * [EventHandlerLibrary] implementation holding this mod's handlers for the base mod's cross-loader client events.
 *
 * Each is built only once the base mod's client side has come up, since the events they are bound to are declared there.
 */
object ToStarsClientEventHandlerLibrary : EventHandlerLibrary(ToStarsMod.modid) {

    /**
     * Turns a set of rotor blades held or worn by an entity at the speed that entity is moving.
     */
    val rotor_blades_movement by this(RotorMovementEventHandler(), ToBaseClientMod.events::client_entity_tick_pre)
        .deferBuild(ToBaseClientMod::onInitialised)

}
