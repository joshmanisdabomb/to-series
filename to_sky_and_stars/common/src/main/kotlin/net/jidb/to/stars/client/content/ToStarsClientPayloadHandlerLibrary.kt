package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ClientPayloadHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.network.handler.EnergyStorageSyncPayloadHandler
import net.jidb.to.stars.client.network.handler.NuclearExplosionPayloadHandler
import net.jidb.to.stars.content.ToStarsPayloadLibrary

object ToStarsClientPayloadHandlerLibrary : ClientPayloadHandlerLibrary(ToStarsMod.MOD_ID) {

    val nuclear_explosion by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.nuclear_explosion.type }, NuclearExplosionPayloadHandler::handle) }
    val energy_storage_sync by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.energy_storage_sync.type }, EnergyStorageSyncPayloadHandler::handle) }

}
