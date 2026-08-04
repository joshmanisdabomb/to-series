package net.jidb.to.base.api.wiki

import com.google.gson.JsonArray
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

/**
 * A data class representing a built wiki article that was loaded from [net.jidb.to.base.client.content.wiki.WikiArticleManager].
 *
 * @property id The unique identifier of the article.
 * @property title The title of this article, as a chat component (literal/translatable).
 * @property short The short title of this article, as a chat component (literal/translatable).
 * @property subtitle The subtitle of this article, as an optional chat component (literal/translatable).
 * @property about The list of "things" (usually resource keys for a real registry) that this article is loosely 'about'.
 * @property redirect The list of "things" (usually resource keys for a real registry) that would redirect the user to this article if they were looking for them.
 * @property parent The "thing" (usually a resource key for a real registry) that this article is grouped under.
 * @property previous The "thing" (usually a resource key for a real registry) that this article is after.
 * @property next The "thing" (usually a resource key for a real registry) that this article is before.
 * @property icon The "thing" (usually a resource key for a real registry) that this article displays as an icon.
 * @property content A map of languages, each with a map of article content i.e. "pages" indexed by their type as a string.
 * @property changelog A list of entries making up the history of what this article is 'about'.
 * @property tags A list of 'tags' for this article, different to Minecraft's tagging system.
 * @since 0.1.0
 */
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

    /**
     * Gets the [WikiArticleContent] for the given language and section.
     *
     * @param language The language to get the content of, falls back to "en_us" if the given language was not defined.
     * @param section The section to get the content of.
     * @return The content, including text and the desired locale.
     * @since 0.1.0
     */
    fun getContent(language: String = "en_us", section: String = "index") = (content[language] ?: content["en_us"])!![section]

}
