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

/**
 * The creative tab library for To Lay the Foundations, which automatically registers creative tabs to Minecraft's [net.minecraft.core.Registry].
 *
 * This mod only provides one [tab].
 *
 * @since 0.0.3
 */
object ToBaseCreativeTabLibrary : SimpleRegistryLibrary<CreativeModeTab>(ToBaseMod.modid), TranslatableLibrary<CreativeModeTab, CreativeModeTab> {

    override val registry = BuiltInRegistries.CREATIVE_MODE_TAB

    /**
     * The tab for the mod, which contains the Researcher's Desk and the test blocks and items.
     *
     * @since 0.0.3
     */
    val tab by this { entry ->
        Services.platform.creativeTabs.builder { parameters, output ->
            output(ItemStack(ToBaseMod.content.blocks.research_desk))
            output(ItemStack(ToBaseMod.content.blocks.test_block))
            output(ItemStack(ToBaseMod.content.blocks.test_block_2))
            output(ItemStack(ToBaseMod.content.items.test_item))
        }
            .title(Component.translatable("itemgroup.$modid.${entry.name}"))
            .icon { ToBaseItemLibrary.icon.create() }
            .build()
    }

    override fun getEntryTranslationKey(entry: Library<CreativeModeTab, CreativeModeTab>.LibraryEntry<out CreativeModeTab, out CreativeModeTab>) = "itemgroup.$modid.${entry.name}"

}
