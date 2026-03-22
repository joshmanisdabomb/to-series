package net.jidb.to.base.wiki

import net.jidb.to.base.wiki.language.WikiLanguage
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor
import org.commonmark.node.*
import org.commonmark.parser.Parser

class WikiArticleContent(val raw: String, locale: String) {

    val language = WikiLanguage.languages.find { it.locale == locale } ?: WikiLanguage.languages.first()

    protected val node: Node
    val yaml: Map<String, List<String>>

    private lateinit var parser: WikiArticleDisplayTokenParser
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

    fun bake(self: Identifier, context: WikiArticleIndex) {
        component = Component.empty()
        parser = WikiArticleDisplayTokenParser(self, context)
        appendChildren(node, Style.EMPTY)
    }

    private fun appendChildren(parent: Node, style: Style) {
        var child = parent.firstChild
        while (child != null) {
            appendNode(child, style)
            child = child.next
        }
    }

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

    private fun appendText(literal: String, style: Style) {
        if (literal.isEmpty()) return
        val parsed = parser.parse(literal, language)
        component.append(parsed.withStyle(style))
    }

    private fun appendLiteral(literal: String, style: Style) {
        if (literal.isEmpty()) return
        component.append(Component.literal(literal).withStyle(style))
    }

    private fun appendBlockSeparator(node: Node) {
        if (node.next != null) {
            appendLiteral("\n\n", Style.EMPTY)
        }
    }

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
