package net.jidb.to.base.fabric.client.event

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.pub.event.ToBaseClientEventLibrary
import net.jidb.to.base.client.pub.event.level.ClientLevelEventContext
import net.jidb.to.base.fabric.library.FabricEventHandlerLibrary

object ToBaseFabricClientEventHandlerLibrary : FabricEventHandlerLibrary(ToBaseMod.modid) {

    val client_level_tick_pre by this(ClientTickEvents.START_LEVEL_TICK, ClientTickEvents.StartLevelTick { level ->
        ToBaseClientEventLibrary.client_level_tick_pre.call(ClientLevelEventContext(level))
    })

}
