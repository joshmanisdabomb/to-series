package net.jidb.to.stars.content

import net.jidb.to.base.api.network.ServerPayloadContext
import net.jidb.to.base.pub.library.PayloadHandlerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.handler.AtomicBombDetonatePayloadHandler

/**
 * [PayloadHandlerLibrary] implementation holding what the server does with each payload the client sends it.
 */
object ToStarsPayloadHandlerLibrary : PayloadHandlerLibrary<ServerPayloadContext>(ToStarsMod.MOD_ID) {

    /**
     * Arms an atomic bomb, in answer to a player confirming it through its interface.
     */
    val atomic_bomb_detonate by this { PayloadHandlerEntry({ ToStarsPayloadLibrary.atomic_bomb_detonate.type }, AtomicBombDetonatePayloadHandler::handle) }

}
