package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.base.client.data.provider.MultiLanguageDataProvider
import net.jidb.to.stars.ToStarsMod
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.TranslatableContents

class ToStarsLanguageDataProvider(tokens: Map<String, Map<String, Component>>, output: PackOutput) : MultiLanguageDataProvider(tokens, output, ToStarsMod.modid) {

    override fun addTranslations() {
        add((ToStarsMod.tabs.tab.displayName.contents as TranslatableContents).key, "To Sky and Stars")
    }

}