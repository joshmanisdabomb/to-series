package net.jidb.to.base.client.data.pub.provider

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.JsonHelper
import net.jidb.to.base.api.helper.JsonHelper.addOrNull
import net.jidb.to.base.api.helper.JsonHelper.addSerialisableArray
import net.jidb.to.base.api.helper.JsonHelper.addStringObject
import net.jidb.to.base.api.helper.JsonHelper.addStrings
import net.jidb.to.base.api.helper.JsonHelper.getOrCreateObject
import net.jidb.to.base.api.helper.JsonHelper.getOrPut
import net.jidb.to.base.api.helper.RegistryHelper
import net.jidb.to.base.api.wiki.language.WikiLanguage
import net.jidb.to.base.client.data.pub.provider.wiki.WikiArticleDataTokenParser
import net.jidb.to.base.client.data.pub.provider.wiki.WikiDataEnforcer
import net.jidb.to.base.mixin.BlockStateBaseAccessor
import net.jidb.to.base.mixin.FireBlockAccessor
import net.jidb.to.base.pub.item.DefaultItemComponentRegistry
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.FireBlock
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.createDirectories
import kotlin.io.path.div
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.walk

/**
 * A [DataProvider] baking the wiki articles of a mod, turning the directory of Markdown and JSON that an article is written as into the one file the game reads.
 *
 * Each article's `index.json` is the whole of the output: its changelog and factsheet are folded into it, its Markdown pages are parsed a locale at a time into a `content` object, and the figures that can be read off the game itself, such as a block's hardness or an item's stack size, are filled in where the author did not write them by hand.
 * The templates are read from where the source lives rather than from the resources directory, which is why the article is never edited where it lands.
 *
 * Where a [WikiDataEnforcer] has been given, anything it expects an article for and did not get is logged and written out as a report alongside the articles.
 *
 * @property output Where the generated files are written.
 * @property lookup The registries the figures on a factsheet are read against.
 * @property templates Where the articles are written, given the folder the generated files are going to.
 * @since 0.1.0
 */
open class WikiDataProvider(val output: PackOutput, val lookup: CompletableFuture<HolderLookup.Provider>, val templates: (output: Path) -> Path) : DataProvider {

    /**
     * Creates a provider reading the articles from a fixed path, for the usual case where it does not depend on where the output is going.
     *
     * @param output Where the generated files are written.
     * @param lookup The registries the figures on a factsheet are read against.
     * @param path Where the articles are written.
     * @since 0.1.0
     */
    constructor(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>, path: Path) : this(output, lookup, { path })

    /**
     * The rule deciding which articles the mod is expected to have, or `null` where nothing is checked.
     *
     * @since 0.1.0
     */
    var enforcer: WikiDataEnforcer? = null
        private set

    /**
     * Sets the rule deciding which articles the mod is expected to have.
     *
     * @param enforcer The rule to check the baked articles against.
     * @return This provider, so that the call can be chained onto its construction.
     * @since 0.1.0
     */
    fun enforce(enforcer: WikiDataEnforcer): WikiDataProvider {
        this.enforcer = enforcer
        return this
    }

    /**
     * Where the baked articles are written to, which is the resource pack rather than the data pack because the wiki is read on the client.
     *
     * @return The path provider the articles are written through.
     * @since 0.1.0
     */
    open fun getPathProvider() = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "wiki/articles")

    override fun run(cached: CachedOutput): CompletableFuture<*> {
        return lookup.thenCompose { provider ->
            val source = templates(output.outputFolder)
            val articles = source.walk().filter { articleMatcher.matches(source.relativize(it)) }
            val json = articles.associate { index ->
                val root = index.parent
                val contents = Files.readString(index)
                val json = JsonHelper.gsonBuilderData.fromJson(contents, JsonObject::class.java)

                val changelog = try {
                    JsonHelper.gsonBuilderData.fromJson(Files.readString(root / "changelog.json"), JsonArray::class.java)
                } catch (_: IOException) {
                    null
                }
                val factsheet = try {
                    JsonHelper.gsonBuilderData.fromJson(Files.readString(root / "factsheet.json"), JsonObject::class.java)
                } catch (_: IOException) {
                    null
                }
                val resources = json.getAsJsonArray("about").mapNotNull {
                    val key = RegistryHelper.createResourceKey(it.asString) ?: return@mapNotNull null
                    val resource = RegistryHelper.getResource(key) ?: return@mapNotNull null
                    key to resource
                }.toMap()

                if (changelog != null) {
                    json.add("changelog", changelog)
                }
                if (factsheet != null) {
                    json.add("factsheet", writeFactsheet(factsheet, resources, provider))
                }

                val parser = WikiArticleDataTokenParser(json)
                json.add("content", writeContent(root, parser))

                val identifier = Identifier.fromNamespaceAndPath(index.parent.parent.parent.fileName.toString(), index.parent.fileName.toString())
                identifier to json
            }

            val out = getPathProvider()
            val missing = enforcer?.enforce(json.values.flatMap {
                val abouts = it.get("about")?.asJsonArray?.toList() ?: emptyList()
                val redirects = it.get("redirect")?.asJsonArray?.toList() ?: emptyList()
                (abouts + redirects).map(JsonElement::getAsString).mapNotNull(RegistryHelper::splitResourceKey)
            })
            if (missing?.isNotEmpty() == true) {
                val report = "Found registry entries with missing articles:\n${missing.joinToString("\n", transform = Any::toString)}"
                ToBaseMod.logger.error(report)
                val path = out.file(Identifier.fromNamespaceAndPath(ToBaseMod.modid, "enforcer_report"), "txt")
                path.parent.createDirectories()
                Files.write(path, report.toByteArray())
            }

            CompletableFuture.allOf(*json.map { (k, v) ->
                //Not interested in debugging why CachedOutput doesn't work here, I don't personally need to cache.
                CompletableFuture.runAsync {
                    val path = out.json(k)
                    path.parent.createDirectories()
                    Files.write(path, JsonHelper.gsonBuilderData.toJson(v).toByteArray())
                }
            }.toTypedArray())
        }
    }

    /**
     * Parses every Markdown page of an article, keyed by locale and then by the name of the page.
     *
     * @param article The directory the article is written in.
     * @param parser The parser resolving the templates the article was written with.
     * @return The parsed content of the article.
     * @since 0.1.0
     */
    protected open fun writeContent(article: Path, parser: WikiArticleDataTokenParser) = JsonObject().apply {
        WikiLanguage.languages.forEach { language ->
            val directory = article.resolve(language.locale)
            add(language.locale, JsonObject().apply {
                directory.listDirectoryEntries("*.md").forEach { page ->
                    val contents = Files.readString(page)
                    addProperty(page.fileName.nameWithoutExtension, parser.parse(contents, language))
                }
            })
        }
    }

    /**
     * Fills in the figures of a factsheet that can be read off the game itself, such as a block's hardness, the states it has and the odds of it catching fire, or an item's stack size and rarity.
     * Anything the author wrote by hand is left alone, so a written figure always wins over the one the game reports.
     *
     * @param input The factsheet as it was written.
     * @param resources The things the article is about, keyed by their registry entry.
     * @param lookup The registries the figures are read against.
     * @return The factsheet, with the figures filled in.
     * @since 0.1.0
     */
    protected open fun writeFactsheet(input: JsonObject, resources: Map<ResourceKey<*>, Any>, lookup: HolderLookup.Provider) = input.apply {
        resources.forEach { (key, resource) ->
            getOrCreateObject(RegistryHelper.keyToString(key)) { original ->
                if (resource is Block) {
                    original.getOrPut("hardness", resource.defaultDestroyTime())
                    original.getOrPut("resistance", resource.explosionResistance)
                    original.getOrPut("friction", resource.friction)

                    val ignite = original.getOrPut("ignite", JsonObject())
                    val burn = original.getOrPut("burn", JsonObject())
                    BuiltInRegistries.BLOCK.entrySet().filter { it.value is FireBlock }.toSortedSet(Comparator.comparing { it.key.identifier() }).forEach { (key, fire) ->
                        ignite.getOrPut(key.identifier().toString(), (fire as FireBlockAccessor).`to_base$getIgniteOdds`().getOrDefault(resource, 0))
                        burn.getOrPut(key.identifier().toString(), (fire as FireBlockAccessor).`to_base$getBurnOdds`().getOrDefault(resource, 0))
                    }

                    if (!original.has("states")) {
                        original.addSerialisableArray("states", resource.stateDefinition.possibleStates.toTypedArray()) { state -> JsonObject().also {
                            it.addProperty("default", state == resource.defaultBlockState())
                            it.addStringObject("properties", state.values.toList().associate { it.property().name to it.valueName() })
                            it.addProperty("light", state.lightEmission)
                            it.addOrNull("map_color", (state as BlockStateBaseAccessor).`to_base$getDefaultMapColor`().col)
                        } }
                    }

                    original.getOrCreateObject("tags") {
                        it.add("minecraft:block", getTagJson(BuiltInRegistries.BLOCK, resource))
                    }

                    //TODO required and recommended tools
                    //TODO ticks randomly, tick speed
                }
                when (resource) {
                    is ItemLike -> {
                        val item = resource.asItem()
                        original.getOrPut("stack_size", DefaultItemComponentRegistry.getDefaultComponentValue(item, DataComponents.MAX_STACK_SIZE, lookup) ?: 64)
                        original.getOrPut("rarity", (DefaultItemComponentRegistry.getDefaultComponentValue(item, DataComponents.RARITY, lookup) ?: Rarity.COMMON).name.lowercase())

                        original.getOrCreateObject("tags") {
                            it.add("minecraft:item", getTagJson(BuiltInRegistries.ITEM, item))
                        }

                        //TODO item dynamic attributes
                    }
                }
            }
        }
    }

    override fun getName() = "To Study the Archives Article Definitions"

    companion object {

        /**
         * Which files under the templates directory are an article, i.e. the `index.json` of one directory per article.
         *
         * @since 0.1.0
         */
        private val articleMatcher = FileSystems.getDefault().getPathMatcher("glob:*/articles/*/index.json")

        /**
         * The tags a registry entry belongs to, written out for a factsheet.
         *
         * @param I The type of the registry entry.
         * @param registry The registry the entry belongs to.
         * @param value The entry whose tags are being written.
         * @return The tags, as an array of their names.
         * @since 0.1.0
         */
        protected fun <I : Any> getTagJson(registry: Registry<I>, value: I) = JsonArray().also {
            it.addStrings(registry.getOrThrow(registry.getResourceKey(value).orElseThrow()).tags().map(TagKey<I>::toString).toList().toTypedArray())
        }

    }

}
