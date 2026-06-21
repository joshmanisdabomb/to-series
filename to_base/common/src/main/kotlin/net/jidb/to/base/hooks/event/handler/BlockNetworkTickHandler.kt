package net.jidb.to.base.hooks.event.handler

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.pub.event.level.ServerLevelEventContext

class BlockNetworkTickHandler : EventHandler<ServerLevelEventContext, Unit>() {

    override fun invoke(context: ServerLevelEventContext) {
        context.level.dataStorage.computeIfAbsent(ToBaseMod.savedData.block_networks).tick(context.level)
    }

}
