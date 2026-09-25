package net.jidb.to.stars.content.key

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

/**
 * [ResourceKeyLibrary] implementation holding the configured features of this mod, i.e. what its world generation places before it is decided where.
 */
object ToStarsConfiguredFeatureLibrary : ResourceKeyLibrary<ConfiguredFeature<*, *>>(ToStarsMod.modid) {

    override val registryKey = Registries.CONFIGURED_FEATURE

    /**
     * Uranium ore as it is generated in stone.
     */
    val uranium_ore by this()

    /**
     * Uranium ore as it is generated in deepslate.
     */
    val deepslate_uranium_ore by this()

}
