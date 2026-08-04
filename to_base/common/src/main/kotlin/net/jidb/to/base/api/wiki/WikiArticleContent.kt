package net.jidb.to.base.api.wiki

import net.jidb.to.base.api.wiki.language.WikiLanguage
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor
import org.commonmark.node.BulletList
import org.commonmark.node.Code
import org.commonmark.node.Emphasis
import org.commonmark.node.HardLineBreak
import org.commonmark.node.Heading
import org.commonmark.node.Link
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.parser.Parser

/**
 * Represents the content of a wiki article, including its raw text, locale, and the parsed YAML front matter metadata.
 * This class handles the baking of the Markdown into styled [Component] objects for Minecraft to render.
 *
 * @property raw The raw text content of the wiki article, including Markdown, inline JSON tokens, and YAML metadata.
 * @param locale The locale string given for this article content.
 * @since 0.1.0
 */
class WikiArticleContent(val raw: String, locale: String) {

    /**
     * The [WikiLanguage] corresponding to the current locale used by the Wiki article content.
     *
     * If a language matching the current locale is not found in the list of available languages, it defaults to the first language in the list (probably [net.jidb.to.base.pub.wiki.language.EnglishWikiLanguage]).
     *
     * @since 0.1.0
     */
    val language = WikiLanguage.languages.find { it.locale == locale } ?: WikiLanguage.languages.first()

    /**
     * The Markdown root node for the article, parsed with [org.commonmark].
     *
     * @since 0.1.0
     */
    private val node: Node

    /**
     * The front matter at the top of the article content.
     *
     * @since 0.1.0
     */
    val yaml: Map<String, List<String>>

    /**
     * The inline JSON token parser, set when [bake] is called by [net.jidb.to.base.client.content.wiki.WikiArticleManager].
     *
     * @since 0.1.0
     */
    private lateinit var parser: WikiArticleDisplayTokenParser

    /**
     * The final parsed root [Component] that Minecraft can display to render this article.
     *
     * @see [net.jidb.to.base.client.content.gui.screens.ResearchScreen]
     * @since 0.1.0
     */
    lateinit var component: MutableComponent
        private set

    init {
        val parser = Parser.builder()
            .extensions(listOf(YamlFrontMatterExtension.create()))
            .build()
        node = parser.parse(raw)

        val yamlVisitor = YamlFrontMatterVisitor()
        node.accept(yamlVisitor)
        yaml = yamlVisitor.data
    }

    /**
     * Builds the final [component] of this [WikiArticleContent] by parsing the Markdown and inline JSON tokens.
     *
     * @param self The [Identifier] of the [WikiArticle] being processed. This is the article [Identifier] this content belongs to.
     * @param context The full index of [WikiArticle]s, providing access to related articles and data.
     * @since 0.1.0
     */
    fun bake(self: Identifier, context: WikiArticleIndex) {
        component = Component.empty()
        parser = WikiArticleDisplayTokenParser(self, context)
        appendChildren(node, Style.EMPTY)
    }

    /**
     * Goes through each child Markdown [Node] of the given parent node and processes them with [appendNode].
     *
     * @param parent The parent node whose children should be processed.
     * @param style The current style to be applied to the children nodes during processing.
     * @since 0.1.0
     */
    private fun appendChildren(parent: Node, style: Style) {
        var child = parent.firstChild
        while (child != null) {
            appendNode(child, style)
            child = child.next
        }
    }

    /**
     * Processes the given Markdown [Node] and decides which `append*` method and [style] changes should be used to add it to the final [component].
     *
     * M<arkdown [Node] types processed here include [Text], [Code], [SoftLineBreak], [HardLineBreak], [StrongEmphasis], [Emphasis], [Link], [Heading], [Paragraph], [BulletList], [OrderedList], and [ListItem].
     *
     * @param node The Markdown [Node] to be processed and appended.
     * @param style The current component [Style] to apply to the [node]'s content.
     * @since 0.1.0
     */
    private fun appendNode(node: Node, style: Style) {
        when (node) {
            is Text -> appendText(node.literal, style)
            is Code -> appendLiteral(node.literal, style.withUnderlined(true))
            is SoftLineBreak -> appendLiteral(" ", style)
            is HardLineBreak -> appendLiteral("\n", style)
            is StrongEmphasis -> appendChildren(node, style.withBold(true))
            is Emphasis -> appendChildren(node, style.withItalic(true))
            is Link -> appendChildren(node, style.withUnderlined(true))
            is Heading -> {
                appendLiteral("${"-".repeat(node.level)} ", style)
                appendChildren(node, style.withBold(true))
                appendBlockSeparator(node)
            }
            is Paragraph -> {
                appendChildren(node, style)
                appendBlockSeparator(node)
            }
            is BulletList -> appendChildren(node, style)
            is OrderedList -> appendChildren(node, style)
            is ListItem -> {
                val prefix = when (node.parent) {
                    is OrderedList -> "${orderedItemNumber(node, node.parent as OrderedList)}. "
                    else -> "• "
                }
                appendLiteral(prefix, style)
                appendChildren(node, style)
                appendLiteral("\n", style)
            }
            else -> appendChildren(node, style)
        }
    }

    /**
     * Appends the given text to the final [component] with the specified style.
     * The text is fed through [WikiArticleDisplayTokenParser], which will parse and modify inline JSON tokens in place.
     *
     * @param literal The text content to append. If the provided string is empty, no action is taken.
     * @param style The style to apply to the appended text.
     * @since 0.1.0
     */
    private fun appendText(literal: String, style: Style) {
        if (literal.isEmpty()) return
        val parsed = parser.parse(literal, language)
        component.append(parsed.withStyle(style))
    }

    /**
     * Appends the given literal text to the final [component] with the specified style.
     * The literal is not fed through [WikiArticleDisplayTokenParser], so inline JSON tokens are not modified.
     *
     * @param literal The text content to append. If the provided string is empty, no action is taken.
     * @param style The style to apply to the appended text.
     * @since 0.1.0
     */
    private fun appendLiteral(literal: String, style: Style) {
        if (literal.isEmpty()) return
        component.append(Component.literal(literal).withStyle(style))
    }

    /**
     * Appends a block separator (double newline) to the content if the given node has a subsequent sibling node.
     *
     * @param node The current Markdown [Node]. If the node has a next sibling, a block separator is appended.
     * @since 0.1.0
     */
    private fun appendBlockSeparator(node: Node) {
        if (node.next != null) {
            appendLiteral("\n\n", Style.EMPTY)
        }
    }

    /**
     * Calculates the order number of a given list item within an ordered list.
     *
     * @param item The list item for which the order number is to be determined.
     * @param list The ordered list that contains the list item.
     * @return The calculated order number of the list item within the ordered list.
     * @since 0.1.0
     */
    private fun orderedItemNumber(item: ListItem, list: OrderedList): Int {
        var number = list.markerStartNumber
        var sibling = item.previous
        while (sibling != null) {
            if (sibling is ListItem) {
                number++
            }
            sibling = sibling.previous
        }
        return number
    }

}
