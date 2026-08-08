package net.jidb.to.stars.network.handler

import net.jidb.to.base.api.network.ServerPayloadContext
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.AtomicBombBlockEntity
import net.jidb.to.stars.inventory.menu.AtomicBombMenu
import net.jidb.to.stars.network.AtomicBombDetonatePayload

/**
 * Arms the atomic bomb whose interface a player has open, in answer to them confirming it.
 */
object AtomicBombDetonatePayloadHandler {

    /**
     * Arms the bomb whose interface the player has open, having first checked they are still standing at it.
     *
     * @param data The payload, which carries nothing.
     * @param context Who sent it.
     */
    @Suppress("detekt.UnusedParameter")
    fun handle(data: AtomicBombDetonatePayload, context: ServerPayloadContext) {
        val bomb = context.player.containerMenu as? AtomicBombMenu ?: return
        if (!bomb.stillValid(context.player)) {
            ToStarsMod.logger.warn("Player {} interacted with invalid menu {}", context.player, bomb)
            return
        }

        (bomb.container as? AtomicBombBlockEntity)?.detonate(context.player)
    }

}
