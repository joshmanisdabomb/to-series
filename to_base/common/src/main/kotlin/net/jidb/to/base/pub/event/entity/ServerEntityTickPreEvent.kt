package net.jidb.to.base.pub.event.entity

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

class ServerEntityTickPreEvent(id: Identifier) : Event<EntityEventContext, Unit>(id)
