package net.jidb.to.stars.content.key

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

object ToStarsConfiguredFeatureLibrary : ResourceKeyLibrary<ConfiguredFeature<*, *>>(ToStarsMod.modid) {

    override val registryKey = Registries.CONFIGURED_FEATURE

    val uranium_ore by this()
    val deepslate_uranium_ore by this()

}