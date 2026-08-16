package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.PayloadLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.AtomicBombDetonatePayload
import net.jidb.to.stars.network.EnergySyncPayload
import net.jidb.to.stars.network.NuclearExplosionPayload
import net.jidb.to.stars.network.ProcessorSyncPayload
import net.jidb.to.stars.network.RotorSyncPayload

object ToStarsPayloadLibrary : PayloadLibrary(ToStarsMod.MOD_ID) {

    val nuclear_explosion by this { NuclearExplosionPayload.Companion }

    val atomic_bomb_detonate by this { AtomicBombDetonatePayload.Companion }

    val energy_storage_sync by this { EnergySyncPayload.Companion }

    val rotor_sync by this { RotorSyncPayload.Companion }

    val processor_sync by this { ProcessorSyncPayload.Companion }

}
