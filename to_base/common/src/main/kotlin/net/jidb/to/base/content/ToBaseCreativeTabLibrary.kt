package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.api.library.TranslatableLibrary
import net.jidb.to.base.service.Services
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object ToBaseCreativeTabLibrary : SimpleRegistryLibrary<CreativeModeTab>(ToBaseMod.modid), TranslatableLibrary<CreativeModeTab, CreativeModeTab> {

    override val registry = BuiltInRegistries.CREATIVE_MODE_TAB

    val tab by this { entry ->
        Services.platform.creativeTabs.builder() { parameters, output ->
            output(ItemStack(ToBaseMod.content.blocks.research_desk))
            output(ItemStack(ToBaseMod.content.blocks.test_block))
            output(ItemStack(ToBaseMod.content.blocks.test_block_2))
            output(ItemStack(ToBaseMod.content.items.test_item))
        }
            .title(Component.translatable("itemgroup.${modid}.${entry.name}"))
            .icon { ItemStack(ToBaseMod.content.items.test_item) }
            .build()
    }

    override fun getEntryTranslationKey(entry: Library<CreativeModeTab, CreativeModeTab>.LibraryEntry<out CreativeModeTab, out CreativeModeTab>) = "itemgroup.${modid}.${entry.name}"

}