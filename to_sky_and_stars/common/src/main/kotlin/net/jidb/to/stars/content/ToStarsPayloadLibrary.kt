package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.PayloadLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.AtomicBombDetonatePayload
import net.jidb.to.stars.network.EnergySyncPayload
import net.jidb.to.stars.network.NuclearExplosionPayload
import net.jidb.to.stars.network.ProcessorSyncPayload
import net.jidb.to.stars.network.RotorSyncPayload

/**
 * [PayloadLibrary] implementation holding the packets this mod sends between the client and the server.
 */
object ToStarsPayloadLibrary : PayloadLibrary(ToStarsMod.MOD_ID) {

    /**
     * Tells the client that a nuclear explosion has gone off, so that it can draw and play it.
     */
    val nuclear_explosion by this { NuclearExplosionPayload.Companion }

    /**
     * Tells the server that a player has armed an atomic bomb.
     */
    val atomic_bomb_detonate by this { AtomicBombDetonatePayload.Companion }

    /**
     * Tells the client how much energy a block is holding.
     */
    val energy_storage_sync by this { EnergySyncPayload.Companion }

    /**
     * Tells the client how fast a set of rotor blades is turning.
     */
    val rotor_sync by this { RotorSyncPayload.Companion }

    /**
     * Tells the client how far through its recipe a processor is.
     */
    val processor_sync by this { ProcessorSyncPayload.Companion }

}
