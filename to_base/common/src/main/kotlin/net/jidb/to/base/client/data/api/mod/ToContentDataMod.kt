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

/**
 * Implemented by a mod that generates data, naming every provider the generator should run for it.
 * The loader projects each read this in their own way, so that the providers themselves are declared once in common code.
 *
 * Everything is optional but [modid], since a mod only names the kinds of data it actually has.
 * See [ToCollectionDataMod] for the collections those providers usually draw from.
 *
 * @since 0.3.0
 */
interface ToContentDataMod {

    /**
     * The mod ID that the generated files are written under.
     *
     * @since 0.3.0
     */
    val modid: String

    /**
     * Builds the provider writing out the translations of this mod, given the tokens gathered from its collections, or `null` where it has none. Defaults to `null`.
     *
     * @since 0.3.0
     */
    val language: ((tokens: Map<String, Map<String, Component>>, output: PackOutput) -> MultiLanguageDataProvider)? get() = null

    /**
     * Builds the providers writing out the models and blockstates of this mod. Defaults to none.
     *
     * @since 0.3.0
     */
    val models: List<(output: PackOutput) -> ModelProvider> get() = emptyList()

    /**
     * Builds the providers writing out the tags of this mod. Defaults to none.
     *
     * @since 0.3.0
     */
    val tags: List<(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>) -> TagsProvider<*>> get() = emptyList()

    /**
     * Builds the providers writing out the loot tables of the blocks of this mod. Defaults to none.
     *
     * @since 0.3.0
     */
    val blockLoot: List<(provider: HolderLookup.Provider) -> BlockLootSubProvider> get() = emptyList()

    /**
     * Builds the providers writing out the loot tables of this mod that belong to no block, such as those of a chest or an entity. Defaults to none.
     *
     * @since 0.8.0
     */
    val generalLoot: List<(provider: HolderLookup.Provider) -> LootTableSubProvider> get() = emptyList()

    /**
     * The providers writing out the advancements of this mod. Defaults to none.
     *
     * @since 0.5.0
     */
    val advancements: List<AdvancementSubProvider> get() = emptyList()

    /**
     * The rules deciding which wiki articles this mod is expected to have, against which the generator checks the articles that exist. Defaults to none.
     *
     * @since 0.3.0
     */
    val wiki: List<WikiDataEnforcer?>? get() = emptyList()

    /**
     * The library holding the game tests of this mod, or `null` where it has none. Defaults to `null`.
     *
     * Naming it here writes out the test instance and the structure of every test it holds, which is what actually makes them runnable; registering the test functions is separate and is done by naming the same library on [net.jidb.to.base.api.mod.ToContentMod.gameTests].
     *
     * @since 1.0.0
     */
    val gameTests: GameTestLibrary? get() = null

    /**
     * The library holding the configured features of this mod, or `null` where it has none. Defaults to `null`.
     *
     * @since 0.3.0
     */
    val configuredFeatures: DatapackLibrary<ConfiguredFeature<*, *>>? get() = null

    /**
     * The library holding the placed features of this mod, or `null` where it has none. Defaults to `null`.
     *
     * @since 0.3.0
     */
    val placedFeatures: DatapackLibrary<PlacedFeature>? get() = null

    /**
     * The libraries holding the biome modifiers of this mod, i.e. what it adds to the biomes that already exist. Defaults to none.
     *
     * @since 0.3.0
     */
    val biomeMods: List<SimpleLibrary<BiomeMod>> get() = emptyList()

}
