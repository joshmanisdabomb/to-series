package net.jidb.to.base.neoforge.client.content.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.data.api.provider.MultiLanguageDataProvider
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

        add("gui.$modid.components.energy.display", "%s / %s")
        add("gui.$modid.components.energy.narration", "Energy %sTE out of %sTE")

        add("tooltip.$modid.number.si", "%s%s %s")
        add("tooltip.$modid.number.si.k", "k")
        add("tooltip.$modid.number.si.m", "M")
        add("tooltip.$modid.number.si.g", "G")
        add("tooltip.$modid.number.si.t", "T")
        add("tooltip.$modid.number.si.p", "P")
        add("tooltip.$modid.number.si.e", "E")

        add("tooltip.$modid.duration.tick", "%s%s tick")
        add("tooltip.$modid.duration.tick.plural", "%s%s ticks")
        add("tooltip.$modid.duration.second", "%s%s second")
        add("tooltip.$modid.duration.second.plural", "%s%s seconds")
        add("tooltip.$modid.duration.minute", "%s%s minute")
        add("tooltip.$modid.duration.minute.plural", "%s%s minutes")
        add("tooltip.$modid.duration.hour", "%s%s hour")
        add("tooltip.$modid.duration.hour.plural", "%s%s hours")
        add("tooltip.$modid.duration.day", "%s%s day")
        add("tooltip.$modid.duration.day.plural", "%s%s days")
        add("tooltip.$modid.duration.week", "%s%s week")
        add("tooltip.$modid.duration.week.plural", "%s%s weeks")
        add("tooltip.$modid.duration.month", "%s%s month")
        add("tooltip.$modid.duration.month.plural", "%s%s months")
        add("tooltip.$modid.duration.year", "%s%s year")
        add("tooltip.$modid.duration.year.plural", "%s%s years")

        add("tooltip.$modid.number.plus", "+%s")

        add("tooltip.$modid.energy.stored", "Stored Energy: %s")
        add("tooltip.$modid.energy.stored.value", "%sTE / %sTE")
        add("tooltip.$modid.energy.stored.fabric", "Fabric Energy Equivalent: %s")
        add("tooltip.$modid.energy.stored.fabric.value", "%sRF / %sRF")
        add("tooltip.$modid.energy.stored.forge", "Forge Energy Equivalent: %s")
        add("tooltip.$modid.energy.stored.forge.value", "%sFE / %sFE")
        add("tooltip.$modid.energy.change", "Change: %s")
        add("tooltip.$modid.energy.change.value", "%sTE/t")
        add("tooltip.$modid.energy.change.avg", "Average Change: %s")
        add("tooltip.$modid.energy.change.avg.value", "%sTE/%s")
        add("tooltip.$modid.energy.insert", "Inserted: %s")
        add("tooltip.$modid.energy.insert.value", "%sTE/t")
        add("tooltip.$modid.energy.insert.avg", "Average Inserted: %s")
        add("tooltip.$modid.energy.insert.avg.value", "%sTE/%s")
        add("tooltip.$modid.energy.extract", "Extracted: %s")
        add("tooltip.$modid.energy.extract.value", "%sTE/t")
        add("tooltip.$modid.energy.extract.avg", "Average Extracted: %s")
        add("tooltip.$modid.energy.extract.avg.value", "%sTE/%s")
        add("tooltip.$modid.energy.result.full", "Full in: %s")
        add("tooltip.$modid.energy.result.full.value", "%s")
        add("tooltip.$modid.energy.result.empty", "Empty in: %s")
        add("tooltip.$modid.energy.result.empty.value", "%s")
        add("tooltip.$modid.energy.max.input", "Maximum Input: %s")
        add("tooltip.$modid.energy.max.input.value", "%sTE/t")
        add("tooltip.$modid.energy.max.output", "Maximum Output: %s")
        add("tooltip.$modid.energy.max.output.value", "%sTE/t")
        add("tooltip.$modid.energy.max.io", "Maximum Input/Output: %s")
        add("tooltip.$modid.energy.max.io.value", "%sTE/t")

        add("tooltip.$modid.more", "Hold down %s for more info...")

        add("key.$modid.keyboard.shift", "Shift")
    }
}