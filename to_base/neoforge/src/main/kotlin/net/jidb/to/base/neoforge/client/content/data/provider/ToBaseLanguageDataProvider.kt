package net.jidb.to.base.neoforge.client.content.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.data.provider.MultiLanguageDataProvider
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.TranslatableContents

class ToBaseLanguageDataProvider(tokens: Map<String, Map<String, Component>>, output: PackOutput) : MultiLanguageDataProvider(tokens, output, ToBaseMod.modid) {
    override fun addTranslations() {
        add((ToBaseMod.tabs.tab.displayName.contents as TranslatableContents).key, "To Lay the Foundations")

        add(ToBaseMod.itemTags.research_desk_unlock, "Researcher's Desk Unlock Items")

        add("container.$modid.research.home", "Back to Homepage")
        add("container.$modid.research.search", "Search Box")
        add("container.$modid.research.search.hint", "Search...")
        add("container.$modid.research.search.button", "Confirm Search")
        add("container.$modid.research.inventory", "Select from Inventory")
        add("container.$modid.research.browser", "Open in Browser")
        add("container.$modid.research.browser.page", "Open Page in Browser")
        add("container.$modid.research.lists.all", "All Pages")
        add("container.$modid.research.lists.minecraft:block", "Blocks")
        add("container.$modid.research.lists.minecraft:item", "Items")
        add("container.$modid.research.lists.minecraft:entity_type", "Mobs")
        add("container.$modid.research.lists.to_base:mod_version", "Mod Versions")
        add("container.$modid.research.lists.to_base:mod", "Mods")
        add("container.$modid.research.lists.to_base:author", "Mod Authors")
        add("container.$modid.research.lists.to_base:concept", "Concepts")
        add("container.$modid.research.lists.to_sky_and_stars", "To Sky and Stars")
        add("container.$modid.research.list.none", "No results found.")
        add("container.$modid.research.list.search", "Search results for \"%s\"")
        add("container.$modid.research.list.list", "All %s")
        add("container.$modid.research.type.minecraft:block", "Block")
        add("container.$modid.research.type.minecraft:item", "Item")
        add("container.$modid.research.type.minecraft:entity_type", "Entity")
        add("container.$modid.research.type.to_base:mod_version", "Mod Version")
        add("container.$modid.research.type.to_base:mod", "Mod")
        add("container.$modid.research.type.to_base:author", "Mod Author")
        add("container.$modid.research.type.to_base:concept", "Concept")
        add("container.$modid.research.type.mixed", "Mixed Types")
        add("container.$modid.research.404", "This article does not yet exist.")
        add("container.$modid.research.500", "This markup is invalid and the mod author needs to fix it.")
    }
}