package net.jidb.to.stars.content.key

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.placement.PlacedFeature

/**
 * [ResourceKeyLibrary] implementation holding the placed features of this mod, i.e. where and how often its configured features are generated.
 */
object ToStarsPlacedFeatureLibrary : ResourceKeyLibrary<PlacedFeature>(ToStarsMod.modid) {

    override val registryKey = Registries.PLACED_FEATURE

    /**
     * Where uranium ore is generated in stone.
     */
    val uranium_ore by this()

    /**
     * Where uranium ore is generated in deepslate.
     */
    val deepslate_uranium_ore by this()

}
