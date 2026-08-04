package net.jidb.to.base.api.platform

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * A [Platform]-specific module that defines a cross-platform contract for handling [TagKey].
 * This module has code for retrieving common `c:` tags from Neoforge and Fabric.
 *
 * @since 0.1.0
 */
abstract class TagsPlatformModule {

    /**
     * All common [Block] tags with fields found in Forge or Fabric, keyed by their lowercase [String] name.
     *
     * @since 0.1.0
     */
    val blockTags: Map<String, TagKey<Block>> by lazy {
        reflectedBlockTags().filter { it.location.namespace == "c" }.associateBy { it.location.path.lowercase() }
    }

    /**
     * All common [Item] tags with fields found in Forge or Fabric, keyed by their lowercase [String] name.
     *
     * @since 0.1.0
     */
    val itemTags: Map<String, TagKey<Item>> by lazy {
        reflectedItemTags().filter { it.location.namespace == "c" }.associateBy { it.location.path.lowercase() }
    }

    /**
     * Retrieves a [List] of [TagKey] for common `c:` [Block] tags available on the current modloader.
     *
     * @return A list of tags represented as a field on this modloader.
     * @since 0.1.0
     */
    protected abstract fun reflectedBlockTags(): List<TagKey<Block>>

    /**
     * Retrieves a [List] of [TagKey] for common `c:` [Item] tags available on the current modloader.
     *
     * @return A list of tags represented as a field on this modloader.
     * @since 0.1.0
     */
    protected abstract fun reflectedItemTags(): List<TagKey<Item>>

    /**
     * Getter for a common `c:` [Block] tag by its lowercase name.
     *
     * @param name The name of the tag to retrieve. The name is case-insensitive.
     * @return The [TagKey] associated with the given name, or `null` if no matching tag is found.
     * @since 0.1.0
     */
    fun getCommonBlock(name: String) = blockTags[name.lowercase()]

    /**
     * Getter for a common `c:` [Item] tag by its lowercase name.
     *
     * @param name The name of the tag to retrieve. The name is case-insensitive.
     * @return The [TagKey] associated with the given name, or `null` if no matching tag is found.
     * @since 0.1.0
     */
    fun getCommonItem(name: String) = itemTags[name.lowercase()]

}
