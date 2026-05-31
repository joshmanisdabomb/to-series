package net.jidb.to.base.client.data.provider

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.data.provider.wiki.WikiArticleDataTokenParser
import net.jidb.to.base.client.data.provider.wiki.WikiDataEnforcer
import net.jidb.to.base.data.ToDataItemHelper
import net.jidb.to.base.helper.*
import net.jidb.to.base.mixin.BlockStateBaseAccessor
import net.jidb.to.base.mixin.FireBlockAccessor
import net.jidb.to.base.wiki.language.WikiLanguage
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
import kotlin.io.path.*

open class WikiDataProvider(val output: PackOutput, val lookup: CompletableFuture<HolderLookup.Provider>, val templates: (output: Path) -> Path) : DataProvider {

    constructor(output: PackOutput, lookup: CompletableFuture<HolderLookup.Provider>, path: Path) : this(output, lookup, { path })

    var enforcer: WikiDataEnforcer? = null
        private set

    fun enforce(enforcer: WikiDataEnforcer): WikiDataProvider {
        this.enforcer = enforcer
        return this
    }

    open fun getPathProvider() = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "wiki/articles")

    override fun run(cached: CachedOutput): CompletableFuture<*> {
        return lookup.thenCompose { provider ->
            val source = templates(output.outputFolder)
            val articles = source.walk().filter { ARTICLE_MATCHER.matches(source.relativize(it) ) }
            val json = articles.associate { index ->
                val root = index.parent
                val contents = Files.readString(index)
                val json = DATA_GSON.fromJson(contents, JsonObject::class.java)

                val changelog = try {
                    DATA_GSON.fromJson(Files.readString(root / "changelog.json"), JsonArray::class.java)
                } catch (e: IOException) {
                    null
                }
                val factsheet = try {
                    DATA_GSON.fromJson(Files.readString(root / "factsheet.json"), JsonObject::class.java)
                } catch (e: IOException) {
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

            val missing = enforcer?.enforce(json.values.flatMap {
                val abouts = it.get("about")?.asJsonArray?.toList() ?: emptyList()
                val redirects = it.get("redirect")?.asJsonArray?.toList() ?: emptyList()
                (abouts + redirects).map(JsonElement::getAsString).mapNotNull(RegistryHelper::createResourceKey)
            })
            if (missing?.isNotEmpty() == true) {
                ToBaseMod.logger.error("Found registry entries with missing articles:\n${missing.joinToString("\n", transform = Any::toString)}")
                throw RuntimeException("Found ${missing.count()} registry entries with missing articles.")
            }

            val out = getPathProvider()
            CompletableFuture.allOf(*json.map { (k, v) ->
                //Not interested in debugging why CachedOutput doesn't work here, I don't personally need to cache.
                CompletableFuture.runAsync {
                    val path = out.json(k)
                    path.parent.createDirectories()
                    Files.write(path, DATA_GSON.toJson(v).toByteArray())
                }
            }.toTypedArray())
        }
    }

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
                        original.getOrPut("stack_size", ToDataItemHelper.getDefaultComponentValue(item, DataComponents.MAX_STACK_SIZE, lookup) ?: 64)
                        original.getOrPut("rarity", (ToDataItemHelper.getDefaultComponentValue(item, DataComponents.RARITY, lookup) ?: Rarity.COMMON).name.lowercase())

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
        private val ARTICLE_MATCHER = FileSystems.getDefault().getPathMatcher("glob:*/articles/*/index.json")

        protected fun <I : Any> getTagJson(registry: Registry<I>, value: I) = JsonArray().also {
            it.addStrings(registry.getOrThrow(registry.getResourceKey(value).orElseThrow()).tags().map(TagKey<I>::toString).toList().toTypedArray())
        }
    }

}