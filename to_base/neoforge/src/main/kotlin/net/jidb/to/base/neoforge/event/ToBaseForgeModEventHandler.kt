package net.jidb.to.base.neoforge.event

import net.jidb.to.base.pub.event.ToBaseEventLibrary
import net.jidb.to.base.pub.event.item.ModifyItemComponentEventContext
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object ToBaseForgeModEventHandler {

    @SubscribeEvent
    fun onDefaultComponentRegister(event: ModifyDefaultComponentsEvent) {
        ToBaseEventLibrary.modify_default_components.call(ModifyItemComponentEventContext({ item, patch ->
            event.modify(item) { builder, holder, item -> patch(builder, item) }
        }, { predicate, patch ->
            event.modifyMatching({ item, components -> predicate(item) }) { builder, holder, item -> patch(builder, item) }
        }))
    }

}
