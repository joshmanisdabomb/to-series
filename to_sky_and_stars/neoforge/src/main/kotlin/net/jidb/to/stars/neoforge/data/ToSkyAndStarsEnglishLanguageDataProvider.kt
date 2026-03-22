package net.jidb.to.stars.neoforge.data

import net.jidb.to.stars.ToSkyAndStarsMod
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.contents.TranslatableContents
import net.neoforged.neoforge.common.data.LanguageProvider

class ToSkyAndStarsEnglishLanguageDataProvider(output: PackOutput) : LanguageProvider(output, ToSkyAndStarsMod.MOD_ID, "en_us") {
    override fun addTranslations() {
        add(ToSkyAndStarsMod.blocks.test_block, "Test Starry Block")
        add(ToSkyAndStarsMod.items.test_item, "Test Starry Item")
        add((ToSkyAndStarsMod.tabs.tab.displayName.contents as TranslatableContents).key, "To Sky and Stars")
    }
}