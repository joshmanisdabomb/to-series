package net.jidb.to.base.fabric.event

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.fabric.library.FabricEventHandlerLibrary
import net.jidb.to.base.pub.event.ToBaseEventLibrary
import net.jidb.to.base.pub.event.item.ModifyItemComponentEventContext
import net.jidb.to.base.pub.event.level.ServerLevelEventContext

object ToBaseFabricEventHandlerLibrary : FabricEventHandlerLibrary(ToBaseMod.modid) {

    val server_level_tick_pre by this(ServerTickEvents.START_LEVEL_TICK, ServerTickEvents.StartLevelTick { level ->
        ToBaseEventLibrary.server_level_tick_pre.call(ServerLevelEventContext(level))
    })
    val modify_default_components by this(DefaultItemComponentEvents.MODIFY, DefaultItemComponentEvents.ModifyCallback {
        ToBaseEventLibrary.modify_default_components.call(ModifyItemComponentEventContext({ item, patch ->
            it.modify(item.asItem()) { builder, holder, item -> patch(builder, item) }
        }, { predicate, patch ->
            it.modify(predicate) { builder, holder, item -> patch(builder, item) }
        }))
    })

}
