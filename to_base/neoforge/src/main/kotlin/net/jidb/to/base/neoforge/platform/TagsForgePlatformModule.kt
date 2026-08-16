package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.TagsPlatformModule
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

object TagsForgePlatformModule : TagsPlatformModule() {

    override fun reflectedBlockTags() = Tags.Blocks::class.java.declaredFields.filter { field ->
        java.lang.reflect.Modifier.isPublic(field.modifiers) &&
            java.lang.reflect.Modifier.isStatic(field.modifiers) &&
            java.lang.reflect.Modifier.isFinal(field.modifiers) &&
            TagKey::class.java.isAssignableFrom(field.type)
    }.map { it.get(null) as TagKey<Block> }

    override fun reflectedItemTags() = Tags.Items::class.java.declaredFields.filter { field ->
        java.lang.reflect.Modifier.isPublic(field.modifiers) &&
            java.lang.reflect.Modifier.isStatic(field.modifiers) &&
            java.lang.reflect.Modifier.isFinal(field.modifiers) &&
            TagKey::class.java.isAssignableFrom(field.type)
    }.map { it.get(null) as TagKey<Item> }

}
