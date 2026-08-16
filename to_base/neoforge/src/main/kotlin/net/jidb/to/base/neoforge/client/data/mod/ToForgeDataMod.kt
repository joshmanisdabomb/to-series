package net.jidb.to.base.neoforge.client.data.mod

import net.jidb.to.base.client.data.api.collection.event.LangClientDataCollectionEvent
import net.jidb.to.base.client.data.api.mod.ToCollectionDataMod
import net.jidb.to.base.client.data.api.provider.MultiLanguageDataProvider
import net.jidb.to.base.client.data.pub.provider.WikiDataProvider
import net.jidb.to.base.data.api.AggregateTagLookup
import net.jidb.to.base.data.api.ToDataHelper
import net.jidb.to.base.data.api.collection.event.ConfiguredFeatureDataCollectionEvent
import net.jidb.to.base.data.api.collection.event.PlacedFeatureDataCollectionEvent
import net.jidb.to.base.data.pub.provider.CopyDataProvider
import net.jidb.to.base.data.pub.provider.DeleteDataProvider
import net.jidb.to.base.data.pub.provider.GameTestStructureDataProvider
import net.jidb.to.base.neoforge.client.data.collection.provider.CollectionModelDataProvider
import net.jidb.to.base.neoforge.data.collection.provider.CollectionBlockLootDataProvider
import net.jidb.to.base.neoforge.data.collection.provider.CollectionBlockTagDataProvider
import net.jidb.to.base.neoforge.data.collection.provider.CollectionGeneralLootDataProvider
import net.jidb.to.base.neoforge.data.collection.provider.CollectionItemTagDataProvider
import net.jidb.to.base.neoforge.data.collection.provider.CollectionRecipeDataProvider
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.advancements.AdvancementProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry
import net.minecraft.data.tags.TagsProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.div
import kotlin.io.path.isDirectory

abstract class ToForgeDataMod(override val event: GatherDataEvent.Client) : IToForgeDataMod, ToContentForgeDataMod, ToCollectionDataMod {

    override val modid = event.modContainer.modId

    override val language: ((tokens: Map<String, Map<String, Component>>, output: PackOutput) -> MultiLanguageDataProvider)? = { tokens, output -> MultiLanguageDataProvider(tokens, output, modid) }

    protected open val resources: Path = Path.of(event.generator.packOutput.outputFolder.toString()
        .replace("neoforge", "common")
        .replace("generated", "resources"))

    protected open val deleteExisting: List<Path> = ToDataHelper.getReplaceableResources(resources)

    protected open val copyTemplateModels: Boolean = true

    protected open val copyResources: Boolean = true

    override fun generate() {
        collections?.build()
        val colls = collections?.collections ?: emptyList()

        if (deleteExisting.isNotEmpty()) {
            event.createProvider { DeleteDataProvider(it, deleteExisting) }
        }

        if (copyTemplateModels) {
            val source = resources.parent / "templates" / "models"
            if (source.isDirectory()) {
                event.createProvider { CopyDataProvider(it, source, it.outputFolder / "assets" / modid / "models") { dst, src ->
                    if (!src.isDirectory()) dst.resolveSibling("template_" + dst.fileName.toString()) else dst
                } }
            }
        }

        val langEvent = LangClientDataCollectionEvent()
        val initTokens = langEvent.process(colls)
        language?.also { event.createProvider { output -> it(initTokens ?: emptyMap(), output) } }

        event.createProvider { CollectionModelDataProvider(colls, it, modid) }
        val collectionBlockTagProvider = event.createProvider { output, provider -> CollectionBlockTagDataProvider(colls, output, provider, modid) }
        event.createProvider { output, provider -> CollectionItemTagDataProvider(colls, output, provider, modid) }
        event.createProvider { output, lookup -> LootTableProvider(output, mutableSetOf(), listOf(
            SubProviderEntry({ CollectionBlockLootDataProvider(colls, it) }, LootContextParamSets.BLOCK),
            SubProviderEntry({ CollectionGeneralLootDataProvider(colls, it) }, LootContextParamSets.ALL_PARAMS)
        ), lookup) }
        event.createProvider { output, lookup -> CollectionRecipeDataProvider.Runner(colls, output, lookup, modid) }

        languages.forEach { event.createProvider(it) }
        models.forEach { event.createProvider(it) }
        blockLoot.map { provider -> GatherDataEvent.DataProviderFromOutputLookup { output, lookup -> LootTableProvider(output, mutableSetOf(), listOf(
            SubProviderEntry(provider, LootContextParamSets.BLOCK)
        ), lookup) } }.forEach { event.createProvider(it) }
        generalLoot.map { provider -> GatherDataEvent.DataProviderFromOutputLookup { output, lookup -> LootTableProvider(output, mutableSetOf(), listOf(
            SubProviderEntry(provider, LootContextParamSets.ALL_PARAMS)
        ), lookup) } }.forEach { event.createProvider(it) }
        recipes.forEach { event.createProvider(it) }
        if (advancements.isNotEmpty()) {
            event.createProvider { output, lookup -> AdvancementProvider(output, lookup, advancements) }
        }
        particles.forEach { event.createProvider(it) }
        sounds.forEach { event.createProvider(it) }

        gameTests?.also { library -> event.createProvider { GameTestStructureDataProvider(it, library) } }

        event.createDatapackRegistryObjects(RegistrySetBuilder()
            .add(Registries.TEST_INSTANCE) {
                val library = gameTests ?: return@add
                library.bootstrap(it)
            }
            .add(Registries.CONFIGURED_FEATURE) {
                ConfiguredFeatureDataCollectionEvent(it).process(colls)
                val library = configuredFeatures ?: return@add
                library.context = it
                library.build()
            }
            .add(Registries.PLACED_FEATURE) {
                PlacedFeatureDataCollectionEvent(it).process(colls)
                val library = placedFeatures ?: return@add
                library.context = it
                library.build()
            }
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS) {
                val library = biomeModifiers ?: return@add
                library.context = it
                library.build()

                for (library2 in biomeMods) {
                    library2.values.forEach { m ->
                        m.features.forEach { mf ->
                            val biomes = it.lookup(Registries.BIOME)
                            val pfeatures = it.lookup(Registries.PLACED_FEATURE)
                            BiomeModifiers.AddFeaturesBiomeModifier(
                                HolderSet.direct(mf.biomes.map(biomes::getOrThrow) + mf.tags.flatMap(biomes::getOrThrow)),
                                HolderSet.direct(mf.features.map(pfeatures::getOrThrow)),
                                mf.step
                            )
                        }
                    }
                }
            }
            .add(Registries.DAMAGE_TYPE) {
                val library = damageTypes ?: return@add
                library.context = it
                library.build()
            }
            .add(Registries.JUKEBOX_SONG) {
                val library = music ?: return@add
                library.context = it
                library.build()
            }
        )

        val tagProviders = tags.map { event.createProvider(it) }
        val blockTagLookups = tagProviders.filterIsInstance<BlockTagsProvider>().map { it.contentsGetter() } + collectionBlockTagProvider.contentsGetter()
        val blockTagLookup: CompletableFuture<TagsProvider.TagLookup<Block>> = CompletableFuture.supplyAsync { AggregateTagLookup(blockTagLookups.map(CompletableFuture<TagsProvider.TagLookup<Block>>::get)) }
        copyTags.forEach { event.createProvider { output, lookup -> it(output, lookup, blockTagLookup) } }

        wiki?.map { enforcer -> GatherDataEvent.DataProviderFromOutputLookup { output, lookup ->
            val provider = WikiDataProvider(output, lookup, resources.parent / "templates" / "wiki")
            if (enforcer != null) provider.enforce(enforcer)
            provider
        } }?.forEach(event::createProvider)

        if (copyResources) {
            event.createProvider { CopyDataProvider(it, it.outputFolder, resources) }
        }
    }

}
