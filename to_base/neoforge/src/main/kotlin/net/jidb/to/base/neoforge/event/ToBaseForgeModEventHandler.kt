package net.jidb.to.base.neoforge.event

import net.jidb.to.base.pub.event.ToBaseEventLibrary
import net.jidb.to.base.pub.event.item.ModifyItemComponentEventContext
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

/**
 * Holds the listeners that raise the base mod's own cross-loader events from the Neoforge events on the mod bus.
 *
 * @since 0.6.0
 */
object ToBaseForgeModEventHandler {

    /**
     * Raises [ToBaseEventLibrary.modify_default_components] while Neoforge is collecting changes to the components an item carries by default.
     *
     * @param event The Neoforge event.
     * @since 0.6.0
     */
    @SubscribeEvent
    fun onDefaultComponentRegister(event: ModifyDefaultComponentsEvent) {
        ToBaseEventLibrary.modify_default_components.call(ModifyItemComponentEventContext({ item, patch ->
            event.modify(item) { builder, holder, item -> patch(builder, item) }
        }, { predicate, patch ->
            event.modifyMatching({ item, components -> predicate(item) }) { builder, holder, item -> patch(builder, item) }
        }))
    }

}
