package net.jidb.to.base.fabric.client.event

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.pub.event.ToBaseClientEventLibrary
import net.jidb.to.base.client.pub.event.level.ClientLevelEventContext
import net.jidb.to.base.fabric.library.FabricEventHandlerLibrary

/**
 * [FabricEventHandlerLibrary] implementation holding the callbacks that raise the base mod's own cross-loader client events from Fabric's.
 *
 * @since 0.8.0
 */
object ToBaseFabricClientEventHandlerLibrary : FabricEventHandlerLibrary(ToBaseMod.modid) {

    /**
     * Raises [net.jidb.to.base.client.pub.event.ToBaseClientEventLibrary.client_level_tick_pre] from Fabric's own start-of-level-tick event.
     *
     * @since 0.8.0
     */
    val client_level_tick_pre by this(ClientTickEvents.START_LEVEL_TICK, ClientTickEvents.StartLevelTick { level ->
        ToBaseClientEventLibrary.client_level_tick_pre.call(ClientLevelEventContext(level))
    })

}
