package net.jidb.to.base.client.pub.event.level

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

/**
 * An [Event] that fires before a client level ticks.
 * See [net.jidb.to.base.pub.event.level.ServerLevelTickPreEvent] for the server-side counterpart.
 *
 * @param id A unique [Identifier] for the event.
 * @since 0.8.0
 */
class ClientLevelTickPreEvent(id: Identifier) : Event<ClientLevelEventContext, Unit>(id)
