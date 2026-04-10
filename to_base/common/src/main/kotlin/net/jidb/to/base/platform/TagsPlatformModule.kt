package net.jidb.to.base.platform

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

abstract class TagsPlatformModule {

    val blockTags: Map<String, TagKey<Block>> by lazy {
        reflectedBlockTags().filter { it.location.namespace == "c" }.associateBy { it.location.path.lowercase() }
    }
    val itemTags: Map<String, TagKey<Item>> by lazy {
        reflectedItemTags().filter { it.location.namespace == "c" }.associateBy { it.location.path.lowercase() }
    }

    protected abstract fun reflectedBlockTags(): List<TagKey<Block>>
    protected abstract fun reflectedItemTags(): List<TagKey<Item>>

    fun getCommonBlock(name: String) = blockTags[name.lowercase()]
    fun getCommonItem(name: String) = itemTags[name.lowercase()]

}
