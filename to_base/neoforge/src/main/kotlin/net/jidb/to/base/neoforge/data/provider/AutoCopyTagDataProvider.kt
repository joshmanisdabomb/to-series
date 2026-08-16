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

        val vanillaBlockTags by lazy { BlockTags::class.java.declaredFields.filter { field ->
            java.lang.reflect.Modifier.isPublic(field.modifiers) &&
                java.lang.reflect.Modifier.isStatic(field.modifiers) &&
                java.lang.reflect.Modifier.isFinal(field.modifiers) &&
                TagKey::class.java.isAssignableFrom(field.type)
        }.map { it.get(null) as TagKey<Block> }.associateBy { it.location.path.lowercase() } }

        val vanillaItemTags by lazy { ItemTags::class.java.declaredFields.filter { field ->
            java.lang.reflect.Modifier.isPublic(field.modifiers) &&
                java.lang.reflect.Modifier.isStatic(field.modifiers) &&
                java.lang.reflect.Modifier.isFinal(field.modifiers) &&
                TagKey::class.java.isAssignableFrom(field.type)
        }.map { it.get(null) as TagKey<Item> }.associateBy { it.location.path.lowercase() } }

    }

}
