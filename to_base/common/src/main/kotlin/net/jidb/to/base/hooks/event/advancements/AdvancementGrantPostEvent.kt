package net.jidb.to.base.hooks.event.advancements

import net.jidb.to.base.event.Event
import net.minecraft.resources.Identifier

class AdvancementGrantPostEvent(id: Identifier) : Event<AdvancementEventContext, Unit>(id)