package net.jidb.to.base.neoforge.content.data

import net.jidb.to.base.ToBaseMod
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.contents.TranslatableContents
import net.neoforged.neoforge.common.data.LanguageProvider

class ToBaseEnglishLanguageDataProvider(output: PackOutput) : LanguageProvider(output, ToBaseMod.modid, "en_us") {
    override fun addTranslations() {
        add(ToBaseMod.blocks.test_block, "Test Block")
        add(ToBaseMod.blocks.test_block_2, "Test Block 2")
        add(ToBaseMod.items.test_item, "Test Item")
        add((ToBaseMod.tabs.tab.displayName.contents as TranslatableContents).key, "To Lay the Foundations")

        add(ToBaseMod.blocks.research_desk, "Researcher's Desk")

        addTag({ ToBaseMod.itemTags.research_desk_unlock }, "Researcher's Desk Unlock Items")

        add("container.${ToBaseMod.modid}.research.home", "Back to Homepage")
        add("container.${ToBaseMod.modid}.research.search", "Search Box")
        add("container.${ToBaseMod.modid}.research.search.hint", "Search...")
        add("container.${ToBaseMod.modid}.research.search.button", "Confirm Search")
        add("container.${ToBaseMod.modid}.research.inventory", "Select from Inventory")
        add("container.${ToBaseMod.modid}.research.browser", "Open in Browser")
        add("container.${ToBaseMod.modid}.research.browser.page", "Open Page in Browser")
        add("container.${ToBaseMod.modid}.research.lists.all", "All Pages")
        add("container.${ToBaseMod.modid}.research.lists.minecraft:block", "Blocks")
        add("container.${ToBaseMod.modid}.research.lists.minecraft:item", "Items")
        add("container.${ToBaseMod.modid}.research.lists.minecraft:entity_type", "Mobs")
        add("container.${ToBaseMod.modid}.research.lists.to_base:mod_version", "Mod Versions")
        add("container.${ToBaseMod.modid}.research.lists.to_base:mod", "Mods")
        add("container.${ToBaseMod.modid}.research.lists.to_base:author", "Mod Authors")
        add("container.${ToBaseMod.modid}.research.lists.to_base:concept", "Concepts")
        add("container.${ToBaseMod.modid}.research.lists.to_sky_and_stars", "To Sky and Stars")
        add("container.${ToBaseMod.modid}.research.list.none", "No results found.")
        add("container.${ToBaseMod.modid}.research.list.search", "Search results for \"%s\"")
        add("container.${ToBaseMod.modid}.research.list.list", "All %s")
        add("container.${ToBaseMod.modid}.research.type.minecraft:block", "Block")
        add("container.${ToBaseMod.modid}.research.type.minecraft:item", "Item")
        add("container.${ToBaseMod.modid}.research.type.minecraft:entity_type", "Entity")
        add("container.${ToBaseMod.modid}.research.type.to_base:mod_version", "Mod Version")
        add("container.${ToBaseMod.modid}.research.type.to_base:mod", "Mod")
        add("container.${ToBaseMod.modid}.research.type.to_base:author", "Mod Author")
        add("container.${ToBaseMod.modid}.research.type.to_base:concept", "Cnncept")
        add("container.${ToBaseMod.modid}.research.404", "This article does not yet exist.")
        add("container.${ToBaseMod.modid}.research.500", "This markup is invalid and the mod author needs to fix it.")
    }
}