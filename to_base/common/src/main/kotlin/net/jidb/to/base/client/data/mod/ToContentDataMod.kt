package net.jidb.to.base.client.data.mod

import net.jidb.to.base.client.data.provider.MultiLanguageDataProvider
import net.jidb.to.base.client.data.provider.wiki.WikiDataEnforcer
import net.jidb.to.base.data.library.DatapackLibrary
import net.jidb.to.base.level.biome.BiomeMod
import net.jidb.to.base.library.SimpleLibrary
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.advancements.AdvancementSubProvider
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import java.util.concurrent.CompletableFuture

interface ToContentDataMod {

    val modid: String

    val language: ((tokens: Map<String, Map<String, Component>>, output: PackOutput) -> MultiLanguageDataProvider)? get() = null
    val models: List<(output: PackOutput) -> ModelProvider> get() = emptyList()
    val tags: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>) -> TagsProvider<*>> get() = emptyList()
    val blockLoot: List<(provider: HolderLookup.Provider) -> BlockLootSubProvider> get() = emptyList()
    val advancements: List<AdvancementSubProvider> get() = emptyList()
    val wiki: List<WikiDataEnforcer?>? get() = emptyList()

    val configuredFeatures: DatapackLibrary<ConfiguredFeature<*, *>>? get() = null
    val placedFeatures: DatapackLibrary<PlacedFeature>? get() = null
    val biomeMods: List<SimpleLibrary<BiomeMod>> get() = emptyList()

}
