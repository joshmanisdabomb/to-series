package net.jidb.to.stars.content

import net.jidb.to.base.library.PayloadLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.AtomicBombDetonatePayload
import net.jidb.to.stars.network.NuclearExplosionPayload

object ToStarsPayloadLibrary : PayloadLibrary(ToStarsMod.MOD_ID) {

    val nuclear_explosion by this { NuclearExplosionPayload.Companion }
    val atomic_bomb_detonate by this { AtomicBombDetonatePayload.Companion }

}