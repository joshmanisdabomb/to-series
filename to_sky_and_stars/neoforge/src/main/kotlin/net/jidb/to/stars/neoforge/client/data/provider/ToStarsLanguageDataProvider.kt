package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.base.client.data.provider.MultiLanguageDataProvider
import net.jidb.to.stars.ToStarsMod
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.TranslatableContents

class ToStarsLanguageDataProvider(tokens: Map<String, Map<String, Component>>, output: PackOutput) : MultiLanguageDataProvider(tokens, output, ToStarsMod.modid) {

    override fun addTranslations() {
        add((ToStarsMod.tabs.tab.displayName.contents as TranslatableContents).key, "To Sky and Stars")

        add("gui.$modid.atomic_bomb.explosive", "Add 1 TNT")
        add("gui.$modid.atomic_bomb.bullet", "Add an Enriched Uranium Nugget")
        add("gui.$modid.atomic_bomb.fuel", "Add up to %s Enriched Uranium\nor %s Blocks of Enriched Uranium")
        add("gui.$modid.atomic_bomb.detonate", "Detonate!")
        add("gui.$modid.atomic_bomb.detonate.error", "Missing requirements:")
        add("gui.$modid.atomic_bomb.detonate.error.explosive", "Slot 1: TNT")
        add("gui.$modid.atomic_bomb.detonate.error.bullet", "Slot 2: Enriched Uranium Nugget")
        add("gui.$modid.atomic_bomb.detonate.error.fuel", "Slot 3: Enriched Uranium")
        add("gui.$modid.atomic_bomb.detonate.info", "Explosion Strength: %s\nFuse Time: %s")
        add("gui.$modid.atomic_bomb.detonate.info.strength", "%s")
        add("gui.$modid.atomic_bomb.detonate.info.fuse", "%s seconds")

        add("subtitles.$modid.entity.generic.nuke_small", "Nuclear Explosion")
        add("subtitles.$modid.entity.generic.nuke_large", "Nuclear Explosion")
        add("subtitles.$modid.entity.atomic_bomb.activate", "Atomic Bomb activated")
        add("subtitles.$modid.entity.atomic_bomb.timer", "Atomic Bomb ticks")
        add("subtitles.$modid.entity.atomic_bomb.cut", "Atomic Bomb defused")

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
    }

}