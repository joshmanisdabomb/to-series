package net.jidb.to.base.hooks.event.handler

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.EventHandlerLibrary

object ToBaseEventHandlerLibrary : EventHandlerLibrary(ToBaseMod.modid) {

    val block_network_tick by this(BlockNetworkTickHandler(), ToBaseMod.events::server_level_tick_pre)

}
