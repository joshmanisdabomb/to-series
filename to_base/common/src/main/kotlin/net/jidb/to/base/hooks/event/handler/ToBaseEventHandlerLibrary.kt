package net.jidb.to.base.hooks.event.handler

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.EventHandlerLibrary

/**
 * [EventHandlerLibrary] implementation holding the event handlers that the base mod itself needs.
 *
 * @since 0.6.0
 */
object ToBaseEventHandlerLibrary : EventHandlerLibrary(ToBaseMod.modid) {

    /**
     * Ticks the block networks of a server level, before everything else that happens on that tick.
     *
     * @since 0.6.0
     */
    val block_network_tick by this(BlockNetworkTickHandler(), ToBaseMod.events::server_level_tick_pre)

}
