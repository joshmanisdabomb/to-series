package net.jidb.to.stars.neoforge.data

import net.jidb.to.stars.ToStarsMod
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.contents.TranslatableContents
import net.neoforged.neoforge.common.data.LanguageProvider

class ToStarsEnglishLanguageDataProvider(output: PackOutput) : LanguageProvider(output, ToStarsMod.MOD_ID, "en_us") {
    override fun addTranslations() {
        add(ToStarsMod.blocks.nuclear_waste, "Nuclear Waste")
        add(ToStarsMod.blocks.nuclear_fire, "Nuclear Fire")

        add(ToStarsMod.items.test_item, "Test Starry Item")
        add((ToStarsMod.tabs.tab.displayName.contents as TranslatableContents).key, "To Sky and Stars")
    }
}