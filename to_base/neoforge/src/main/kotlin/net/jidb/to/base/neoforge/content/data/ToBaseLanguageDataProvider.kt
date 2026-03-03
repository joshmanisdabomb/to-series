package net.jidb.to.base.neoforge.content.data

import net.jidb.to.base.ToBaseMod
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.contents.TranslatableContents
import net.neoforged.fml.ModList
import net.neoforged.neoforge.common.data.LanguageProvider

class ToBaseLanguageDataProvider(output: PackOutput) : LanguageProvider(output, ToBaseMod.MOD_ID, "en_us") {
    override fun addTranslations() {
        add(ToBaseMod.blocks.test_block, "Test Block")
        add(ToBaseMod.blocks.test_block_2, "Test Block 2")
        add(ToBaseMod.items.test_item, "Test Item")
        add((ToBaseMod.tabs.tab.displayName.contents as TranslatableContents).key, "To Lay the Foundations")
    }
}