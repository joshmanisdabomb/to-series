package net.jidb.to.stars.client.content

import net.jidb.to.base.client.library.ClientPayloadHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.network.handler.NuclearExplosionPayloadHandler
import net.jidb.to.stars.content.ToStarsPayloadLibrary

object ToStarsClientPayloadHandlerLibrary : ClientPayloadHandlerLibrary(ToStarsMod.MOD_ID) {

    val nuclear_explosion by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.nuclear_explosion.type }, NuclearExplosionPayloadHandler::handle) }

}
