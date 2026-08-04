package net.jidb.to.base.client.pub.event

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.pub.event.entity.ClientEntityTickPreEvent
import net.jidb.to.base.client.pub.event.level.ClientLevelTickPreEvent

/**
 * [SimpleLibrary] implementation that declares the cross-platform events To Lay the Foundations fires on the client, and provides access to them in one place.
 * These are kept apart from [net.jidb.to.base.pub.event.ToBaseEventLibrary] so that nothing on the server side can attach to an event that will never fire there.
 *
 * @since 0.8.0
 */
object ToBaseClientEventLibrary : SimpleLibrary<Event<*, *>>(ToBaseMod.modid) {

    /**
     * Fires before a client level ticks.
     *
     * @since 0.8.0
     */
    val client_level_tick_pre by this { ClientLevelTickPreEvent(it.id) }

    /**
     * Fires before an entity ticks on the client.
     *
     * @since 0.8.0
     */
    val client_entity_tick_pre by this { ClientEntityTickPreEvent(it.id) }

}
