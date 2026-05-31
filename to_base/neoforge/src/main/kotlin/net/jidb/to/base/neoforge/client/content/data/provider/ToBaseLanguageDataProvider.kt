package net.jidb.to.base.neoforge.client.content.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.data.provider.MultiLanguageDataProvider
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.TranslatableContents

class ToBaseLanguageDataProvider(tokens: Map<String, Map<String, Component>>, output: PackOutput) : MultiLanguageDataProvider(tokens, output, ToBaseMod.modid) {
    override fun addTranslations() {
        add((ToBaseMod.content.tabs.tab.displayName.contents as TranslatableContents).key, "To Lay the Foundations")

        add(ToBaseMod.content.itemTags.research_desk_unlock, "Researcher's Desk Unlock Items")

        add("gui.$modid.research.home", "Back to Homepage")
        add("gui.$modid.research.search", "Search Box")
        add("gui.$modid.research.search.hint", "Search...")
        add("gui.$modid.research.search.button", "Confirm Search")
        add("gui.$modid.research.inventory", "Select from Inventory")
        add("gui.$modid.research.browser", "Open in Browser")
        add("gui.$modid.research.browser.page", "Open Page in Browser")
        add("gui.$modid.research.lists.all", "All Pages")
        add("gui.$modid.research.lists.minecraft:block", "Blocks")
        add("gui.$modid.research.lists.minecraft:item", "Items")
        add("gui.$modid.research.lists.minecraft:entity_type", "Mobs")
        add("gui.$modid.research.lists.to_base:mod_version", "Mod Versions")
        add("gui.$modid.research.lists.to_base:mod", "Mods")
        add("gui.$modid.research.lists.to_base:author", "Mod Authors")
        add("gui.$modid.research.lists.to_base:concept", "Concepts")
        add("gui.$modid.research.lists.to_sky_and_stars", "To Sky and Stars")
        add("gui.$modid.research.list.none", "No results found.")
        add("gui.$modid.research.list.search", "Search results for \"%s\"")
        add("gui.$modid.research.list.list", "All %s")
        add("gui.$modid.research.type.minecraft:block", "Block")
        add("gui.$modid.research.type.minecraft:item", "Item")
        add("gui.$modid.research.type.minecraft:entity_type", "Entity")
        add("gui.$modid.research.type.to_base:mod_version", "Mod Version")
        add("gui.$modid.research.type.to_base:mod", "Mod")
        add("gui.$modid.research.type.to_base:author", "Mod Author")
        add("gui.$modid.research.type.to_base:concept", "Concept")
        add("gui.$modid.research.type.mixed", "Mixed Types")
        add("gui.$modid.research.404", "This article does not yet exist.")
        add("gui.$modid.research.500", "This markup is invalid and the mod author needs to fix it.")
    }
}