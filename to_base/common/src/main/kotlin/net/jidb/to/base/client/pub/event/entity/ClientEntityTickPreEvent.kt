package net.jidb.to.base.client.pub.event.entity

import net.jidb.to.base.api.event.Event
import net.jidb.to.base.pub.event.entity.EntityEventContext
import net.minecraft.resources.Identifier

class ClientEntityTickPreEvent(id: Identifier) : Event<EntityEventContext, Unit>(id)
