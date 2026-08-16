package net.jidb.to.base.pub.event.item

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

class ModifyDefaultItemComponentsEvent(id: Identifier) : Event<ModifyItemComponentEventContext, Unit>(id)
