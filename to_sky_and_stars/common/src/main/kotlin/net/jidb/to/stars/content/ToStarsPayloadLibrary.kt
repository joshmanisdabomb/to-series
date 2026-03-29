package net.jidb.to.stars.content

import net.jidb.to.base.library.PayloadLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.NuclearExplosionPayload

object ToStarsPayloadLibrary : PayloadLibrary(ToStarsMod.MOD_ID, ToStarsMod.payloadHandlers) {

    val nuclear_explosion by this { NuclearExplosionPayload.Companion }

}