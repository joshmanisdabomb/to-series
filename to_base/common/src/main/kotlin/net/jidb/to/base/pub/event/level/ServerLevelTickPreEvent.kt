package net.jidb.to.base.pub.event.level

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

class ServerLevelTickPreEvent(id: Identifier) : Event<ServerLevelEventContext, Unit>(id)
