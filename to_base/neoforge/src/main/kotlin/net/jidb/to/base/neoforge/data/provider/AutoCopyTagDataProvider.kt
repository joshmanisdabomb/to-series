package net.jidb.to.base.neoforge.data.provider

import net.jidb.to.base.service.Services
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider
import java.util.concurrent.CompletableFuture

/**
 * A [BlockTagCopyingItemTagProvider] copying a block tag onto the items of those blocks wherever the two obviously correspond, so that the pair need not be written out twice.
 * A tag is matched by name: the vanilla and common tags are paired up by the last part of their own names, and a mod's own tags are paired up by the key they were declared under.
 *
 * A block tag with nothing in it is skipped, since copying it would write out an empty item tag.
 *
 * @param blockTags The mod's own block tags, keyed by whatever pairs them with an item tag.
 * @param itemTags The mod's own item tags, keyed the same way.
 * @param output Where the generated files are written.
 * @param provider The registries the tags are built against.
 * @property lookup What each block tag actually contains, which is how an empty one is spotted.
 * @param modid The mod ID the files are written under.
 * @since 0.3.0
 */
open class AutoCopyTagDataProvider(protected val blockTags: Map<Identifier, TagKey<Block>>, protected val itemTags: Map<Identifier, TagKey<Item>>, output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, protected val lookup: CompletableFuture<TagLookup<Block>>, modid: String) : BlockTagCopyingItemTagProvider(output, provider, lookup, modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        val lookup = lookup.get()
        vanillaBlockTags.forEach { key, block ->
            if (lookup.apply(block).isEmpty) return@forEach

            val item = vanillaItemTags[key]
            if (item != null) {
                copy(block, item)
                return@forEach
            }

            val c = Services.platform.tags.getCommonItem(key)
            if (c != null) {
                copy(block, c)
                return@forEach
            }
        }
        Services.platform.tags.blockTags.forEach { key, block ->
            if (lookup.apply(block).isEmpty) return@forEach

            val item = vanillaItemTags[key]
            if (item != null) {
                copy(block, item)
            }

            copy(block, TagKey<Item>.create(Registries.ITEM, block.location()))
        }
        blockTags.forEach { (key, block) ->
            if (lookup.apply(block).isEmpty) return@forEach

            val item = itemTags[key]
            if (item != null) {
                copy(block, item)
            }
        }
    }

    override fun getName() = "Auto Copy Tags: ${super.name}"

    companion object {

        /**
         * Every vanilla block tag, keyed by the last part of its own name, read off the class by reflection so that a tag added by a later version is picked up without this having to be touched.
         *
         * @since 0.3.0
         */
        val vanillaBlockTags by lazy { BlockTags::class.java.declaredFields.filter { field ->
            java.lang.reflect.Modifier.isPublic(field.modifiers) &&
                java.lang.reflect.Modifier.isStatic(field.modifiers) &&
                java.lang.reflect.Modifier.isFinal(field.modifiers) &&
                TagKey::class.java.isAssignableFrom(field.type)
        }.map { it.get(null) as TagKey<Block> }.associateBy { it.location.path.lowercase() } }

        /**
         * Every vanilla item tag, keyed by the last part of its own name, read off the class by reflection so that a tag added by a later version is picked up without this having to be touched.
         *
         * @since 0.3.0
         */
        val vanillaItemTags by lazy { ItemTags::class.java.declaredFields.filter { field ->
            java.lang.reflect.Modifier.isPublic(field.modifiers) &&
                java.lang.reflect.Modifier.isStatic(field.modifiers) &&
                java.lang.reflect.Modifier.isFinal(field.modifiers) &&
                TagKey::class.java.isAssignableFrom(field.type)
        }.map { it.get(null) as TagKey<Item> }.associateBy { it.location.path.lowercase() } }

    }

}
