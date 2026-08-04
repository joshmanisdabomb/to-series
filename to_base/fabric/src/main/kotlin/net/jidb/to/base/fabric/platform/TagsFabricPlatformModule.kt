package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.jidb.to.base.api.platform.TagsPlatformModule
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * [TagsPlatformModule] implementation for Fabric, whose common tags are Fabric's own conventional ones.
 *
 * They are read off the class by reflection rather than named one at a time, so that a tag added by a later version of the API is picked up without this having to be touched.
 *
 * @since 0.1.0
 */
object TagsFabricPlatformModule : TagsPlatformModule() {

    override fun reflectedBlockTags() = ConventionalBlockTags::class.java.declaredFields.filter { field ->
        java.lang.reflect.Modifier.isPublic(field.modifiers) &&
            java.lang.reflect.Modifier.isStatic(field.modifiers) &&
            java.lang.reflect.Modifier.isFinal(field.modifiers) &&
            TagKey::class.java.isAssignableFrom(field.type)
    }.map { it.get(null) as TagKey<Block> }

    override fun reflectedItemTags() = ConventionalItemTags::class.java.declaredFields.filter { field ->
        java.lang.reflect.Modifier.isPublic(field.modifiers) &&
            java.lang.reflect.Modifier.isStatic(field.modifiers) &&
            java.lang.reflect.Modifier.isFinal(field.modifiers) &&
            TagKey::class.java.isAssignableFrom(field.type)
    }.map { it.get(null) as TagKey<Item> }

}
