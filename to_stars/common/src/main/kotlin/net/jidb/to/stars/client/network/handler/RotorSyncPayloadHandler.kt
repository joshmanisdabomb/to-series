package net.jidb.to.stars.client.network.handler

import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.RotorSyncPayload
import kotlin.jvm.optionals.getOrNull

object RotorSyncPayloadHandler {

    fun handle(data: RotorSyncPayload, context: ClientPayloadContext) {
        if (!context.level.isLoaded(data.pos)) return
        val be = context.level.getBlockEntity(data.pos, ToStarsMod.blockEntities.rotor_blades).getOrNull() ?: return
        be.speed = data.speed
    }

}
