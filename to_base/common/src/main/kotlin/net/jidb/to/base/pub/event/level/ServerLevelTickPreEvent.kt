package net.jidb.to.base.pub.event.level

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

/**
 * An [Event] that fires before a server level ticks.
 * See [net.jidb.to.base.client.pub.event.level.ClientLevelTickPreEvent] for the client-side counterpart.
 *
 * @param id A unique [Identifier] for the event.
 * @since 0.6.0
 */
class ServerLevelTickPreEvent(id: Identifier) : Event<ServerLevelEventContext, Unit>(id)
