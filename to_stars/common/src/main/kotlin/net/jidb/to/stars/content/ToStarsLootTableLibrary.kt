package net.jidb.to.stars.content

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.storage.loot.LootTable

/**
 * [ResourceKeyLibrary] implementation holding the loot tables of this mod that belong to no block.
 */
object ToStarsLootTableLibrary : ResourceKeyLibrary<LootTable>(ToStarsMod.modid) {

    override val registryKey = Registries.LOOT_TABLE

    /**
     * What the first player on the server to set off a nuclear explosion is given.
     */
    val advancement_nuke_race by this(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "advancements/nuke_race"))

}
