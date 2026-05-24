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
    }

}