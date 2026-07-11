package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.base.client.data.api.provider.MultiLanguageDataProvider
import net.jidb.to.stars.ToStarsMod
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.TranslatableContents

class ToStarsLanguageDataProvider(tokens: Map<String, Map<String, Component>>, output: PackOutput) : MultiLanguageDataProvider(tokens, output, ToStarsMod.modid) {

    override fun addTranslations() {
        add((ToStarsMod.tabs.tab.displayName.contents as TranslatableContents).key, "To Sky and Stars")

        add("death.attack.$modid.heated", $$"%1$s couldn't stand the heat from a generator")
        add("death.attack.$modid.heated.player", $$"%1$s couldn't stand the heat while trying to escape %2$s")
        add("death.attack.$modid.boiled", $$"%1$s was boiled alive")
        add("death.attack.$modid.boiled.player", $$"%1$s was boiled alive while trying to escape %2$s")

        add("gui.$modid.atomic_bomb.explosive", "Add 1 TNT")
        add("gui.$modid.atomic_bomb.bullet", "Add an Enriched Uranium Nugget")
        add("gui.$modid.atomic_bomb.fuel", "Add up to %s Enriched Uranium\nor %s Blocks of Enriched Uranium")
        add("gui.$modid.atomic_bomb.detonate", "Detonate!")
        add("gui.$modid.atomic_bomb.detonate.error", "Missing requirements:")
        add("gui.$modid.atomic_bomb.detonate.error.explosive", "Slot 1: TNT")
        add("gui.$modid.atomic_bomb.detonate.error.bullet", "Slot 2: Enriched Uranium Nugget")
        add("gui.$modid.atomic_bomb.detonate.error.fuel", "Slot 3: Enriched Uranium")

        add("subtitles.$modid.entity.generic.nuke_small", "Nuclear Explosion")
        add("subtitles.$modid.entity.generic.nuke_large", "Nuclear Explosion")
        add("subtitles.$modid.entity.atomic_bomb.activate", "Atomic Bomb activated")
        add("subtitles.$modid.entity.atomic_bomb.timer", "Atomic Bomb ticks")
        add("subtitles.$modid.entity.atomic_bomb.cut", "Atomic Bomb defused")
        add("subtitles.$modid.block.generator.crackle", "Heat Generator crackles")
        add("subtitles.$modid.block.generator.empty", "Heat Generator cooling")

        add("advancements.$modid.root.title", "To Sky and Stars")
        add("advancements.$modid.root.description", "Space race and missile mod.")
        add("advancements.$modid.uranium.title", "Uranium Fever")
        add("advancements.$modid.uranium.description", "Mine Uranium")
        add("advancements.$modid.enrichment.title", "Enrichment Activities")
        add("advancements.$modid.enrichment.description", "Refine Uranium in a Centrifuge")
        add("advancements.$modid.nuke.title", "The World is the Problem")
        add("advancements.$modid.nuke.description", "Detonate an Atomic Bomb (must use GUI)")
        add("advancements.$modid.nuke_race.title", "Nuclear Arms Race")
        add("advancements.$modid.nuke_race.description", "Be the first person on the server to detonate an Atomic Bomb")

        add("tooltip.$modid.atomic_bomb.strength", "Explosion Strength: %s")
        add("tooltip.$modid.atomic_bomb.strength.value", "%s")
        add("tooltip.$modid.atomic_bomb.fuse", "Fuse Time: %s")
        add("tooltip.$modid.atomic_bomb.fuse.value", "%ss")
        add("tooltip.$modid.machine.tier", "Machine Tier: %s")
        add("tooltip.$modid.machine.tier.value", "%s")
        add("tooltip.$modid.power_cable.loss", "Energy Loss: %s")
        add("tooltip.$modid.power_cable.loss.value", "%s%%/block")
        add("tooltip.$modid.generator.heat", "Base Fuel Rate: %s")
        add("tooltip.$modid.generator.heat.value", "+%s°C/s")
        add("tooltip.$modid.generator.speed", "Fuel Consumption: %s")
        add("tooltip.$modid.generator.speed.value", "%s%%")
        add("tooltip.$modid.generator.initial", "Minimum Temperature: %s")
        add("tooltip.$modid.generator.initial.value", "%s°C")
        add("tooltip.$modid.generator.range", "Maximum Temperature: %s")
        add("tooltip.$modid.generator.range.value", "%s°C")
        add("tooltip.$modid.generator.bonus", "Maximum Temperature Bonus: %s")
        add("tooltip.$modid.generator.bonus.value", "+%s°C")
        add("tooltip.$modid.generator.cooling", "Cooling Rate: %s")
        add("tooltip.$modid.generator.cooling.value", "%s%%/t")
        add("tooltip.$modid.generator.current", "Current Temperature: %s")
        add("tooltip.$modid.generator.current.value", "%s°C")
        add("tooltip.$modid.generator.target", "Target Temperature: %s")
        add("tooltip.$modid.generator.target.value", "%s°C")
        add("tooltip.$modid.generator.change", "Change: %s")
        add("tooltip.$modid.generator.change.value", "%s°C/s")
        add("tooltip.$modid.generator.remaining", "Time Remaining: %s")
        add("tooltip.$modid.generator.remaining.value", "%s")
        add("tooltip.$modid.generator.result.full", "Full in: %s")
        add("tooltip.$modid.generator.result.full.value", "%s")
        add("tooltip.$modid.generator.result.empty", "Empty in: %s")
        add("tooltip.$modid.generator.result.empty.value", "%s")
        add("tooltip.$modid.generator.fuel.duration", "Burn Duration: %s")
        add("tooltip.$modid.generator.fuel.duration.value", "%s")
        add("tooltip.$modid.generator.fuel.duration.base", "Base Burn Duration: %s")
        add("tooltip.$modid.generator.fuel.duration.base.value", "%s")
        add("tooltip.$modid.generator.fuel.heat", "Fuel Rate: %s")
        add("tooltip.$modid.generator.fuel.heat.value", "+%s°C/s%s")
        add("tooltip.$modid.generator.fuel.multiplier.short", " (x%s)")
        add("tooltip.$modid.generator.fuel.multiplier", "Type Heat Multiplier: %s")
        add("tooltip.$modid.generator.fuel.multiplier.value", "%s%%")
        add("tooltip.$modid.generator.fuel.total", "Total Fuel: %s")
        add("tooltip.$modid.generator.fuel.total.value", "%s°C")
        add("tooltip.$modid.generator.fuel.stack", "Stack Total Fuel: %s")
        add("tooltip.$modid.generator.fuel.stack.value", "%s°C%s")
        add("tooltip.$modid.generator.fuel.count", " (%s)")
        add("tooltip.$modid.generator.fuel.fill", "Insert for Maximum Temperature: %s")
        add("tooltip.$modid.generator.fuel.fill.value", "%s")
        add("tooltip.$modid.generator.fuel.fill.base", "Insert for Minimum to Maximum Temperature: %s")
        add("tooltip.$modid.generator.fuel.fill.base.value", "%s")
    }

}