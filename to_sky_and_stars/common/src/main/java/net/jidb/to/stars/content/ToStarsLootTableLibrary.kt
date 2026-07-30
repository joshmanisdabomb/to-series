package net.jidb.to.stars.content

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable

object ToStarsLootTableLibrary : ResourceKeyLibrary<LootTable>(ToStarsMod.modid) {

    override val registryKey = Registries.LOOT_TABLE

    val advancement_nuke_race by this(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "advancements/nuke_race"))

}
