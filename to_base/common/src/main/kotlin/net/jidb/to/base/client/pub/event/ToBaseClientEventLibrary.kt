package net.jidb.to.base.client.pub.event

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.pub.event.entity.ClientEntityTickPreEvent
import net.jidb.to.base.client.pub.event.level.ClientLevelTickPreEvent

object ToBaseClientEventLibrary : SimpleLibrary<Event<*, *>>(ToBaseMod.modid) {

    val client_level_tick_pre by this { ClientLevelTickPreEvent(it.id) }
    val client_entity_tick_pre by this { ClientEntityTickPreEvent(it.id) }

}