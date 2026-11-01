package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.world.item.ToolMaterial

object ToStarsToolMaterialLibrary : SimpleLibrary<ToolMaterial>(ToStarsMod.MOD_ID) {

    /**
     * The steel tool material tier, mines the same as iron with unlimited durability, and slightly increased speed, but 0 enchantability.
     */
    val steel by this { ToolMaterial(ToStarsMod.blockTags.incorrect_for_steel_tool, 0, 7.0F, 2.0F, 1, ToStarsMod.itemTags.steel_tool_materials) }

}
