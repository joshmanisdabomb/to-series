package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.advancements.AtomicBombAdvancementTrigger
import net.jidb.to.stars.advancements.RaceAdvancementTrigger
import net.minecraft.advancements.triggers.CriterionTrigger
import net.minecraft.core.registries.BuiltInRegistries

/**
 * [SimpleRegistryLibrary] implementation holding the criteria that this mod's advancements can be granted by.
 */
object ToStarsAdvancementTriggerLibrary : SimpleRegistryLibrary<CriterionTrigger<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.TRIGGER_TYPES

    /**
     * Granted when an atomic bomb goes off.
     */
    val atomic_bomb by this { AtomicBombAdvancementTrigger() }

    /**
     * Granted when the player is the first on the server to reach something.
     */
    val race by this { RaceAdvancementTrigger() }

}
