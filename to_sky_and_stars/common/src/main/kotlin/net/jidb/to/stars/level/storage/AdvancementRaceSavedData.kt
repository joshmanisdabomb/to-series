package net.jidb.to.stars.level.storage

import com.mojang.serialization.Codec
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityReference
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData

class AdvancementRaceSavedData(map: Map<Identifier, EntityReference<Player>>) : SavedData() {

    private val map = map.toMutableMap()

    constructor() : this(mapOf())

    operator fun get(advancement: Identifier) = map[advancement]

    operator fun set(advancement: Identifier, player: ServerPlayer) {
        map[advancement] = EntityReference.of(player) ?: return
        setDirty()
    }

    companion object {
        val codec = Codec.unboundedMap(Identifier.CODEC, EntityReference.codec<Player>())
            .xmap(::AdvancementRaceSavedData, AdvancementRaceSavedData::map)
    }

}
