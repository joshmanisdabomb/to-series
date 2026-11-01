package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.DatapackLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.info.MachineTier
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup
import net.minecraft.world.item.equipment.trim.TrimMaterial

/**
 * [DatapackLibrary] implementation generating the trim materials of the mod.
 */
object ToStarsTrimMaterialDataLibrary : DatapackLibrary<TrimMaterial>(ToStarsMod.modid) {

    override val registryKey = Registries.TRIM_MATERIAL

    /**
     * Trim material for steel ingot.
     */
    val steel by this { TrimMaterial(MaterialAssetGroup.create("steel", mapOf(ToStarsMod.armorMaterials.steel.assetId to "steel_darker")), Component.translatable(
        "trim_material.$modid.steel").withStyle(Style.EMPTY.withColor(MachineTier.TWO.chatColor))) }

}
