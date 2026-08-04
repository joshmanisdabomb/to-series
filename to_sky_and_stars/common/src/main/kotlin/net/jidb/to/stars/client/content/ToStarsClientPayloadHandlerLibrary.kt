package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ClientPayloadHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.network.handler.EnergySyncPayloadHandler
import net.jidb.to.stars.client.network.handler.NuclearExplosionPayloadHandler
import net.jidb.to.stars.client.network.handler.ProcessorSyncPayloadHandler
import net.jidb.to.stars.client.network.handler.RotorSyncPayloadHandler
import net.jidb.to.stars.content.ToStarsPayloadLibrary

/**
 * [ClientPayloadHandlerLibrary] implementation holding what the client does with each payload the server sends it.
 */
object ToStarsClientPayloadHandlerLibrary : ClientPayloadHandlerLibrary(ToStarsMod.MOD_ID) {

    /**
     * Draws and plays a nuclear explosion the server has set off.
     */
    val nuclear_explosion by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.nuclear_explosion.type }, NuclearExplosionPayloadHandler::handle) }

    /**
     * Takes on how much energy a block is holding.
     */
    val energy_storage_sync by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.energy_storage_sync.type }, EnergySyncPayloadHandler::handle) }

    /**
     * Takes on how fast a set of rotor blades is turning.
     */
    val rotor_sync by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.rotor_sync.type }, RotorSyncPayloadHandler::handle) }

    /**
     * Takes on how far through its recipe a processor is.
     */
    val processor_sync by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.processor_sync.type }, ProcessorSyncPayloadHandler::handle) }

}
