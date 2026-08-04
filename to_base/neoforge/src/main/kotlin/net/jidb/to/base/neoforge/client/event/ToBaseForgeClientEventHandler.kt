package net.jidb.to.base.neoforge.client.event

import net.jidb.to.base.client.pub.event.ToBaseClientEventLibrary
import net.jidb.to.base.pub.event.entity.EntityEventContext
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

/**
 * Holds the listeners that raise the base mod's own cross-loader client events from the Neoforge events on the game bus.
 *
 * @since 0.5.0
 */
object ToBaseForgeClientEventHandler {

    /**
     * Raises [ToBaseClientEventLibrary.client_entity_tick_pre] at the start of an entity's tick on the client.
     *
     * @param event The Neoforge event.
     * @since 0.5.0
     */
    @SubscribeEvent
    fun onEntityPreTickEvent(event: EntityTickEvent.Pre) {
        if (event.entity.level().isClientSide) {
            ToBaseClientEventLibrary.client_entity_tick_pre.call(EntityEventContext(event.entity))
        }
    }

}
