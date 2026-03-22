package net.jidb.to.base.wiki

import com.google.gson.JsonArray
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

data class WikiArticle(
    val id: Identifier,
    val title: Component,
    val short: Component,
    val subtitle: Component?,
    val about: List<Pair<Identifier, Identifier>>,
    val redirect: List<Pair<Identifier, Identifier>>,
    val parent: Pair<Identifier, Identifier>?,
    val previous: Pair<Identifier, Identifier>?,
    val next: Pair<Identifier, Identifier>?,
    val icon: List<Pair<Identifier, Identifier>>,
    val content: Map<String, Map<String, WikiArticleContent>>,
    val changelog: JsonArray?,
    val tags: List<Identifier>,
) : Comparable<WikiArticle> {

    override fun compareTo(other: WikiArticle): Int = id.compareTo(other.id)

    fun getContent(language: String = "en_us", section: String = "index"): WikiArticleContent? {
        return (content[language] ?: content["en_us"])!![section]
    }

}