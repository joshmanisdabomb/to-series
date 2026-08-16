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

object WikiArticleManager : PreparableReloadListener {

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

    private fun loadTitle(json: JsonElement?): MutableComponent? {
        if (json == null) return null
        return if (json.isJsonObject) {
            val rkey = RegistryHelper.splitResourceKey(json.asJsonObject.getString("of")!!)
            Component.translatable("${rkey.first.path}.${rkey.second.namespace}.${rkey.second.path}")
        } else {
            Component.literal(json.asString)
        }
    }

    internal fun bake() {
        index.data.forEach { (identifier, article) ->
            article.content.forEach { (locale, areas) -> areas.forEach { (area, content) -> content.bake(identifier, index) } }
        }
    }

}
