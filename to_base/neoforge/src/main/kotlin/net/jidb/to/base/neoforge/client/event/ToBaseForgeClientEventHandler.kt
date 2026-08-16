package net.jidb.to.base.neoforge.client.event

import net.jidb.to.base.client.pub.event.ToBaseClientEventLibrary
import net.jidb.to.base.pub.event.entity.EntityEventContext
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

object ToBaseForgeClientEventHandler {

    @SubscribeEvent
    fun onEntityPreTickEvent(event: EntityTickEvent.Pre) {
        if (event.entity.level().isClientSide) {
            ToBaseClientEventLibrary.client_entity_tick_pre.call(EntityEventContext(event.entity))
        }
    }

}
