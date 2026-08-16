package net.jidb.to.base.client.pub.event.level

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

class ClientLevelTickPreEvent(id: Identifier) : Event<ClientLevelEventContext, Unit>(id)
