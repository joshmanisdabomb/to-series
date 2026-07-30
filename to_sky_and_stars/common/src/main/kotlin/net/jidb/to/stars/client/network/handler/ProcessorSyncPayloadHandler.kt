package net.jidb.to.stars.client.network.handler

import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.ProcessorMenu
import net.jidb.to.stars.network.ProcessorSyncPayload

object ProcessorSyncPayloadHandler {

    fun handle(data: ProcessorSyncPayload, context: ClientPayloadContext) {
        if (!context.level.isLoaded(data.pos)) return
        val processor = context.player.containerMenu as? ProcessorMenu ?: return
        if (!processor.stillValid(context.player)) {
            ToStarsMod.logger.warn("Player {} interacted with invalid menu {}", context.player, processor)
            return
        }

        val pos = processor.clientData?.pos ?: return
        if (pos != data.pos) return

        processor.clientData?.lastRecipe = data.lastRecipe
        processor.clientData?.lastRecipeIcon = data.lastRecipeIcon
    }

}
