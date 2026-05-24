package net.jidb.to.stars.network.handler

import net.jidb.to.base.network.ServerPayloadContext
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.AtomicBombBlockEntity
import net.jidb.to.stars.inventory.menu.AtomicBombMenu
import net.jidb.to.stars.network.AtomicBombDetonatePayload

object AtomicBombDetonatePayloadHandler {
    fun handle(data: AtomicBombDetonatePayload, context: ServerPayloadContext) {
        val bomb = context.player.containerMenu as? AtomicBombMenu ?: return
        if (!context.player.containerMenu.stillValid(context.player)) {
            ToStarsMod.logger.warn("Player {} interacted with invalid menu {}", context.player, context.player.containerMenu)
            return
        }

        (bomb.container as? AtomicBombBlockEntity)?.detonate(context.player)
    }
}