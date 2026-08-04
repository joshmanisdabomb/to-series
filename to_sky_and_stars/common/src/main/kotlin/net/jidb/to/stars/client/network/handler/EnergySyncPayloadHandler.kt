package net.jidb.to.stars.client.network.handler

import net.jidb.to.base.client.api.network.ClientPayloadContext
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.EnergySyncPayload
import kotlin.jvm.optionals.getOrNull

/**
 * Takes on how much energy a power bank is holding, as the server reports it.
 */
object EnergySyncPayloadHandler {

    /**
     * Sets the energy of the power bank the payload names.
     *
     * @param data What the server sent.
     * @param context Where it arrived.
     */
    fun handle(data: EnergySyncPayload, context: ClientPayloadContext) {
        if (!context.level.isLoaded(data.pos)) return
        val be = context.level.getBlockEntity(data.pos, ToStarsMod.blockEntities.power_bank).getOrNull() ?: return
        be.energy.energy = data.energy
    }

}
