package net.jidb.to.base.client.data.api.mod

import net.jidb.to.base.api.level.biome.BiomeMod
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.data.api.provider.MultiLanguageDataProvider
import net.jidb.to.base.client.data.pub.provider.wiki.WikiDataEnforcer
import net.jidb.to.base.data.api.library.DatapackLibrary
import net.jidb.to.base.pub.library.GameTestLibrary
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.advancements.AdvancementSubProvider
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.data.loot.LootTableSubProvider
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
    val generalLoot: List<(provider: HolderLookup.Provider) -> LootTableSubProvider> get() = emptyList()
    val advancements: List<AdvancementSubProvider> get() = emptyList()
    val wiki: List<WikiDataEnforcer?>? get() = emptyList()

    /**
     * The library holding the game tests of this mod, or `null` where it has none. Defaults to `null`.
     *
     * Naming it here writes out the test instance and the structure of every test it holds, which is what actually makes them runnable; registering the test functions is separate and is done by naming the same library on [net.jidb.to.base.api.mod.ToContentMod.gameTests].
     *
     * @since 1.0.0
     */
    val gameTests: GameTestLibrary? get() = null

    val configuredFeatures: DatapackLibrary<ConfiguredFeature<*, *>>? get() = null
    val placedFeatures: DatapackLibrary<PlacedFeature>? get() = null
    val biomeMods: List<SimpleLibrary<BiomeMod>> get() = emptyList()

}
