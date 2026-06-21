package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.advancements.AtomicBombAdvancementTrigger
import net.jidb.to.stars.advancements.RaceAdvancementTrigger
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.registries.BuiltInRegistries

object ToStarsAdvancementTriggerLibrary : SimpleRegistryLibrary<CriterionTrigger<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.TRIGGER_TYPES

    val atomic_bomb by this { AtomicBombAdvancementTrigger() }
    val race by this { RaceAdvancementTrigger() }

}