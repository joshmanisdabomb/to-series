package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ClientPayloadHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.network.handler.EnergySyncPayloadHandler
import net.jidb.to.stars.client.network.handler.NuclearExplosionPayloadHandler
import net.jidb.to.stars.client.network.handler.ProcessorSyncPayloadHandler
import net.jidb.to.stars.client.network.handler.RotorSyncPayloadHandler
import net.jidb.to.stars.content.ToStarsPayloadLibrary

object ToStarsClientPayloadHandlerLibrary : ClientPayloadHandlerLibrary(ToStarsMod.MOD_ID) {

    val nuclear_explosion by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.nuclear_explosion.type }, NuclearExplosionPayloadHandler::handle) }
    val energy_storage_sync by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.energy_storage_sync.type }, EnergySyncPayloadHandler::handle) }
    val rotor_sync by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.rotor_sync.type }, RotorSyncPayloadHandler::handle) }
    val processor_sync by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.processor_sync.type }, ProcessorSyncPayloadHandler::handle) }

}
