package net.jidb.to.stars.client.content

import net.jidb.to.base.client.library.ClientPayloadHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.network.handler.NuclearExplosionPayloadHandler

object ToStarsClientPayloadLibrary : ClientPayloadHandlerLibrary(ToStarsMod.MOD_ID) {

    val nuclear_explosion by this { PayloadHandlerEntry({ ToStarsMod.payloads.nuclear_explosion.type }, NuclearExplosionPayloadHandler::handle) }

}
