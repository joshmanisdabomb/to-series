package net.jidb.to.stars.content.key

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.equipment.trim.TrimMaterial

/**
 * [ResourceKeyLibrary] implementation holding the trim materials of this mod.
 */
object ToStarsTrimMaterialLibrary : ResourceKeyLibrary<TrimMaterial>(ToStarsMod.modid) {

    override val registryKey = Registries.TRIM_MATERIAL

    /**
     * Trim material for steel ingot.
     */
    val steel by this()

}
