package net.jidb.to.stars.client.network.handler

import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.RotorSyncPayload
import kotlin.jvm.optionals.getOrNull

/**
 * Takes on how fast a set of rotor blades is turning, as the server reports it.
 */
object RotorSyncPayloadHandler {

    /**
     * Sets the speed of the blades the payload names.
     *
     * @param data What the server sent.
     * @param context Where it arrived.
     */
    fun handle(data: RotorSyncPayload, context: ClientPayloadContext) {
        if (!context.level.isLoaded(data.pos)) return
        val be = context.level.getBlockEntity(data.pos, ToStarsMod.blockEntities.rotor_blades).getOrNull() ?: return
        be.speed = data.speed
    }

}
