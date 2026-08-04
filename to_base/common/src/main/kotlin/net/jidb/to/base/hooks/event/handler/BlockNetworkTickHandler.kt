package net.jidb.to.base.hooks.event.handler

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.pub.event.level.ServerLevelEventContext

/**
 * [EventHandler] ticking the block networks of a server level, which is what carries the changes queued since the last tick into the networks themselves.
 *
 * @since 0.6.0
 */
class BlockNetworkTickHandler : EventHandler<ServerLevelEventContext, Unit>() {

    override fun invoke(context: ServerLevelEventContext) {
        context.level.dataStorage.computeIfAbsent(ToBaseMod.savedData.block_networks).tick(context.level)
    }

}
