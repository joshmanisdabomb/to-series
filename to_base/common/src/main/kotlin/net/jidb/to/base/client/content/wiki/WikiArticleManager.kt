package net.jidb.to.base.client.content.wiki

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.JsonHelper
import net.jidb.to.base.api.helper.JsonHelper.getString
import net.jidb.to.base.api.helper.RegistryHelper
import net.jidb.to.base.api.wiki.WikiArticle
import net.jidb.to.base.api.wiki.WikiArticleContent
import net.jidb.to.base.api.wiki.WikiArticleIndex
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.Resource
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.io.path.nameWithoutExtension

/**
 * The reload listener that loads the wiki articles out of the resource packs, and holds the index they are read from afterwards.
 * Articles are resources rather than data, so the wiki is available on the client without a server having to send it.
 *
 * An article that fails to parse is logged and dropped rather than failing the reload, so one broken file does not take the whole wiki with it.
 * Once every article is loaded they are baked together by [bake], which is when a cross-reference in one article can be resolved against another.
 *
 * @since 0.1.0
 */
object WikiArticleManager : PreparableReloadListener {

    /**
     * The index of every loaded article, which is what the wiki is searched and read through.
     * Only available once a reload has finished, so reading it beforehand will throw.
     *
     * @since 0.1.0
     */
    lateinit var index: WikiArticleIndex
        private set

    override fun reload(sharedState: PreparableReloadListener.SharedState, backgroundExecutor: Executor, barrier: PreparableReloadListener.PreparationBarrier, applyExectutor: Executor) = CompletableFuture.supplyAsync({
        val manager = sharedState.resourceManager()
        val resources = manager.listResources("wiki/articles") { it.path.endsWith(".json") }
        load(resources)
    }, backgroundExecutor).thenCompose(barrier::wait).thenAcceptAsync({ input ->
        index = WikiArticleIndex(input)
        bake()

        ToBaseMod.logger.info("Loaded ${index.all.size} wiki articles.")
    }, applyExectutor)

    /**
     * Reads every article resource found, dropping and logging any whose JSON does not parse.
     * The article is keyed by its own file name rather than the full resource path, so that an article refers to another by name alone.
     *
     * @param resources The article resources found in the resource packs.
     * @return Each article, indexed by the identifier it was loaded under.
     * @since 0.1.0
     */
    private fun load(resources: Map<Identifier, Resource>) = resources.mapNotNull { (identifier, resource) ->
        try {
            val json = InputStreamReader(resource.open()).use {
                JsonHelper.gsonBuilderData.fromJson(it, JsonObject::class.java)
            }
            val basename = Path.of(identifier.path).nameWithoutExtension
            identifier to load(Identifier.fromNamespaceAndPath(identifier.namespace, basename), json)
        } catch (e: JsonSyntaxException) {
            ToBaseMod.logger.error("Failed to load article: $identifier", e)
            null
        }
    }.toMap()

    /**
     * Reads a single article out of its JSON.
     * The content is not baked here, only stored, because baking needs the whole index to resolve references against.
     *
     * @param identifier The identifier the article is loaded under.
     * @param json The JSON of the article.
     * @return The loaded [WikiArticle].
     * @throws NullPointerException If the article has no title, which every article is required to carry.
     * @since 0.1.0
     */
    internal fun load(identifier: Identifier, json: JsonObject): WikiArticle {
        val title = loadTitle(json.get("title"))!!

        return WikiArticle(
            id = identifier,
            title = title,
            short = loadTitle(json.get("short")) ?: title,
            subtitle = loadTitle(json.get("subtitle")),
            about = (json.getAsJsonArray("about")?.asList() ?: emptyList()).map { RegistryHelper.splitResourceKey(it.asString) },
            redirect = (json.getAsJsonArray("redirect")?.asList() ?: emptyList()).map { RegistryHelper.splitResourceKey(it.asString) },
            parent = json.getString("parent")?.let(RegistryHelper::splitResourceKey),
            previous = json.getString("previous")?.let(RegistryHelper::splitResourceKey),
            next = json.getString("next")?.let(RegistryHelper::splitResourceKey),
            icon = (json.getAsJsonArray("icon")?.asList() ?: emptyList()).map { RegistryHelper.splitResourceKey(it.asString) },
            content = json.getAsJsonObject("content").asMap()
                .mapValues { it.value.asJsonObject.asMap().mapValues { (locale, raw) -> WikiArticleContent(raw.asString, locale) } },
            changelog = json.getAsJsonArray("changelog"),
            tags = json.getAsJsonArray("tags").asList().map { Identifier.parse(it.asString) }
        )
    }

    /**
     * Reads one of an article's titles, which is either a literal string or an object naming the registry entry to take the display name from.
     *
     * @param json The JSON of the title, or `null` where the article carries no such title.
     * @return The title as a component, or `null` where there was none to read.
     * @since 0.1.0
     */
    private fun loadTitle(json: JsonElement?): MutableComponent? {
        if (json == null) return null
        return if (json.isJsonObject) {
            val rkey = RegistryHelper.splitResourceKey(json.asJsonObject.getString("of")!!)
            Component.translatable("${rkey.first.path}.${rkey.second.namespace}.${rkey.second.path}")
        } else {
            Component.literal(json.asString)
        }
    }

    /**
     * Bakes the content of every loaded article, which is what turns its template tokens into the text and links that are displayed.
     * This runs only once the whole index exists, as a token in one article may refer to another.
     *
     * @since 0.1.0
     */
    internal fun bake() {
        index.data.forEach { (identifier, article) ->
            article.content.forEach { (locale, areas) -> areas.forEach { (area, content) -> content.bake(identifier, index) } }
        }
    }

}
