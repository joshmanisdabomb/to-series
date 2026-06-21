package net.jidb.to.base.api.wiki

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import kotlin.jvm.optionals.getOrNull

class WikiArticleIndex(val data: Map<Identifier, WikiArticle>) {
    val all: List<WikiArticle> = data.values.sorted()

    val byRegistry = all.flatMap { article ->
        article.about.map { it.first to article }
    }.groupBy({ it.first }, { it.second })

    val byMod = all.groupBy { it.id.namespace }

    val byResource = all.flatMap { article ->
        article.about.map { it to article } + article.redirect.map { it to article }
    }.groupBy({ it.first }, { it.second })

    val byParent = all.groupBy { it.parent }

    operator fun get(id: Identifier): WikiArticle? = data[id]

    operator fun get(item: Item): List<WikiArticle> {
        val key = BuiltInRegistries.ITEM.getResourceKey(item).getOrNull() ?: return emptyList()
        return byResource[Pair(key.registry(), key.identifier())] ?: emptyList()
    }
}