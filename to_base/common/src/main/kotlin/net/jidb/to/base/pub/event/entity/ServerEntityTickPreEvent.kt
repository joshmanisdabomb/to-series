package net.jidb.to.base.pub.event.entity

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

/**
 * An [Event] that fires before an entity ticks on the server.
 * See [net.jidb.to.base.client.pub.event.entity.ClientEntityTickPreEvent] for the client-side counterpart.
 *
 * @param id A unique [Identifier] for the event.
 * @since 0.8.0
 */
class ServerEntityTickPreEvent(id: Identifier) : Event<EntityEventContext, Unit>(id)
