package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.TicketType

/**
 * [SimpleRegistryLibrary] implementation holding the chunk tickets of this mod, i.e. the reasons it keeps a chunk loaded.
 */
object ToStarsTicketLibrary : SimpleRegistryLibrary<TicketType>(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.TICKET_TYPE

    /**
     * Keeps the chunks around a falling atomic bomb loaded, so that it goes off even where no player is watching.
     */
    val atomic_bomb by this { TicketType(100, TicketType.FLAG_LOADING or TicketType.FLAG_SIMULATION or TicketType.FLAG_KEEP_DIMENSION_ACTIVE) }

}
