package net.jidb.to.base.pub.event.advancements

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

class AdvancementGrantPostEvent(id: Identifier) : Event<AdvancementEventContext, Unit>(id)