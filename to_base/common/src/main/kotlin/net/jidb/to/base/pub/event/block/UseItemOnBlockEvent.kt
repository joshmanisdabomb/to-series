package net.jidb.to.base.pub.event.block

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

class UseItemOnBlockEvent(id: Identifier) : Event<BlockInteractEventContext, BlockInteractEventResult>(id)