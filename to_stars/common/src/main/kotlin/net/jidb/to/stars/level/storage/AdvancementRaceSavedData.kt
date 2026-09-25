package net.jidb.to.stars.level.storage

import com.mojang.serialization.Codec
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityReference
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData

/**
 * The saved data recording which player was first on the server to earn each advancement.
 *
 * @param map Which player was first to each advancement, as it was saved.
 */
class AdvancementRaceSavedData(map: Map<Identifier, EntityReference<Player>>) : SavedData() {

    /**
     * Which player was first to each advancement.
     */
    private val map = map.toMutableMap()

    /**
     * Creates the saved data of a server where nobody has been first to anything yet.
     */
    constructor() : this(mapOf())

    /**
     * Who was first to an advancement.
     *
     * @param advancement The advancement being asked about.
     * @return The player who was first to it, or `null` where nobody has earned it yet.
     */
    operator fun get(advancement: Identifier) = map[advancement]

    /**
     * Records a player as having been first to an advancement.
     *
     * @param advancement The advancement they earned.
     * @param player The player who earned it.
     */
    operator fun set(advancement: Identifier, player: ServerPlayer) {
        map[advancement] = EntityReference.of(player) ?: return
        setDirty()
    }

    companion object {

        /**
         * The codec the record is saved and loaded through.
         */
        val codec = Codec.unboundedMap(Identifier.CODEC, EntityReference.codec<Player>())
            .xmap(::AdvancementRaceSavedData, AdvancementRaceSavedData::map)

    }

}
