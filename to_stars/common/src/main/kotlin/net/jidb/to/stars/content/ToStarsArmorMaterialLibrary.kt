package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAssets

object ToStarsArmorMaterialLibrary : SimpleLibrary<ArmorMaterial>(ToStarsMod.MOD_ID) {

    /**
     * The steel armor material tier, has the same protection values as iron (except BODY armor type for animals - as strong as diamond), unlimited durability, knockback resistance equivalent to netherite, and 0 enchantability.
     */
    //TODO custom steel armor equip sound
    val steel by this { ArmorMaterial(0, mapOf(ArmorType.HELMET to 2, ArmorType.CHESTPLATE to 6, ArmorType.LEGGINGS to 5, ArmorType.BOOTS to 2, ArmorType.BODY to 11), 1, SoundEvents.ARMOR_EQUIP_CHAIN, 0f, 0.1f, ToStarsMod.itemTags.repairs_steel_armor, ResourceKey.create(EquipmentAssets.ROOT_ID, it.id)) }

}
