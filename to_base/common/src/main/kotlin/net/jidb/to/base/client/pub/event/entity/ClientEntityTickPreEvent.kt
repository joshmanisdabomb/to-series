package net.jidb.to.base.client.pub.event.entity

import net.jidb.to.base.api.event.Event
import net.jidb.to.base.pub.event.entity.EntityEventContext
import net.minecraft.resources.Identifier

/**
 * An [Event] that fires before an entity ticks on the client.
 * The context is shared with [net.jidb.to.base.pub.event.entity.ServerEntityTickPreEvent], as an entity is the same on either side; only the event it is delivered through says which one it came from.
 *
 * @param id A unique [Identifier] for the event.
 * @since 0.8.0
 */
class ClientEntityTickPreEvent(id: Identifier) : Event<EntityEventContext, Unit>(id)
