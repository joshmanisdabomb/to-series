package net.jidb.to.stars.content

import net.jidb.to.base.api.network.ServerPayloadContext
import net.jidb.to.base.pub.library.PayloadHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.handler.AtomicBombDetonatePayloadHandler

object ToStarsPayloadHandlerLibrary : PayloadHandlerLibrary<ServerPayloadContext>(ToStarsMod.MOD_ID) {

    val atomic_bomb_detonate by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.atomic_bomb_detonate.type }, AtomicBombDetonatePayloadHandler::handle) }

}
