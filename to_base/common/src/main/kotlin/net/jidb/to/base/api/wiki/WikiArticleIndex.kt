package net.jidb.to.base.api.wiki

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import kotlin.jvm.optionals.getOrNull

/**
 * A class that indexes and organizes all [WikiArticle] objects for other classes to lookup.
 *
 * @constructor Initializes the index with a simple map of [Identifier] to [WikiArticle].
 * @property data A raw data mapping of [Identifier] to [WikiArticle].
 * @since 0.1.0
 */
class WikiArticleIndex(val data: Map<Identifier, WikiArticle>) {

    /**
     * A sorted list of all [WikiArticle] objects in the index.
     * @since 0.1.0
     */
    val all: List<WikiArticle> = data.values.sorted()

    /**
     * Groups articles by the first [Identifier] from their [WikiArticle.about] property.
     * @since 0.1.0
     */
    val byRegistry = all.flatMap { article ->
        article.about.map { it.first to article }
    }.groupBy({ it.first }, { it.second })

    /**
     * Groups articles by the namespace portion of their [Identifier].
     * @since 0.1.0
     */
    val byMod = all.groupBy { it.id.namespace }

    /**
     * Groups articles by both [WikiArticle.about] and [WikiArticle.redirect] properties.
     * @since 0.1.0
     */
    val byResource = all.flatMap { article ->
        article.about.map { it to article } + article.redirect.map { it to article }
    }.groupBy({ it.first }, { it.second })

    /**
     * Groups articles by their [WikiArticle.parent] property.
     * @since 0.1.0
     */
    val byParent = all.groupBy { it.parent }

    /**
     * Retrieves the [WikiArticle] associated with the given [Identifier].
     *
     * @param id The unique [Identifier] used to locate the desired article.
     * @return The [WikiArticle] object corresponding to the provided [Identifier], or `null` if no article exists for the given identifier.
     * @since 0.1.0
     */
    operator fun get(id: Identifier): WikiArticle? = data[id]

    /**
     * Retrieves the list of [WikiArticle] objects associated with the given [Item].
     *
     * @param item The [Item] object used to locate the desired articles.
     * @return A list of [WikiArticle] objects corresponding to the provided [Item], or an empty list if no articles exist for the given item.
     * @since 0.1.0
     */
    operator fun get(item: Item): List<WikiArticle> {
        val key = BuiltInRegistries.ITEM.getResourceKey(item).getOrNull() ?: return emptyList()
        return byResource[Pair(key.registry(), key.identifier())] ?: emptyList()
    }

}
