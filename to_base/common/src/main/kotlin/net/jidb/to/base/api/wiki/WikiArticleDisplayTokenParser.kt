package net.jidb.to.base.api.wiki

import com.google.gson.JsonObject
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.JsonHelper.addBool
import net.jidb.to.base.api.helper.JsonHelper.addString
import net.jidb.to.base.api.helper.JsonHelper.getBool
import net.jidb.to.base.api.helper.RegistryHelper
import net.jidb.to.base.api.wiki.language.WikiLanguage
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.resources.Identifier

/**
 * An inline JSON token parser for baking content in wiki articles for final render and display.
 * This class is responsible for interpreting tokens in between double braces `{{}}` and converting them into styled and formatted [Component] objects.
 *
 * Any article Markdown [org.commonmark.node.Node] text is passed to this parser, and then this parser hands back the component with any JSON tokens extracted and built as content.
 *
 * @property self The identifier of the current [WikiArticle] being processed.
 * @property context The full index of [WikiArticle]s, enabling retrieval of related articles and references.
 * @since 0.1.0
 */
class WikiArticleDisplayTokenParser(val self: Identifier, protected val context: WikiArticleIndex) : WikiArticleTokenParser<MutableComponent, MutableComponent>() {

    /**
     * Stores the final article [org.commonmark.node.Node] text as a Minecraft [Component].
     * Built as the [append] method is called by [parse].
     *
     * The result is used to represent a single segment of a [WikiArticle].
     * @since 0.1.0
     */
    private var component = Component.empty()

    override fun start() {
        component = Component.empty()
    }

    override fun append(append: MutableComponent) {
        component.append(append)
    }

    override fun build() = component

    override fun consumeText(content: String, language: WikiLanguage) = Component.literal(content)

    override fun consumeToken(content: JsonObject, language: WikiLanguage): MutableComponent {
        when (content.get("template")?.asString) {
            "mod_recent_version" -> {
                val versions = context.byParent[context[self]?.about?.first() ?: return Component.empty()] ?: return Component.empty()
                val recent = versions.lastOrNull()?.about?.firstOrNull() ?: return Component.empty()
                val token = JsonObject().apply {
                    addString("template", "referral")
                    addString("of", "${recent.first} / ${recent.second}")
                    addBool("short", true)
                }
                return consumeToken(token, language)
            }
            "version_release" -> return Component.empty()
            "referral", null -> {
                val key = content.get("of")!!.asString
                if (key == "self") {
                    val article = context[self]!!
                    return Component.empty().append(getReferralTitle(content, article, language))
                        .withStyle(Style.EMPTY.withColor(TextColor.DARK_GREEN)
                            .withHoverEvent(HoverEvent.ShowText(article.title)))
                }
                val resource = RegistryHelper.splitResourceKey(key)
                val article = context.byResource[resource]?.first()
                if (article != null) {
                    return Component.empty().append(getReferralTitle(content, article, language))
                        .withStyle(Style.EMPTY.withColor(TextColor.BLUE).withUnderlined(true)
                            .withHoverEvent(HoverEvent.ShowText(article.title))
                            .withClickEvent(WikiArticleLink(article)))
                }
                return Component.literal("${resource.first.path}.${resource.second.namespace}.${resource.second.path}")
                    .withStyle(Style.EMPTY.withColor(TextColor.GOLD).withItalic(true)
                        .withHoverEvent(HoverEvent.ShowText(Component.translatable("gui.${ToBaseMod.modid}.research.404")
                            .withStyle(Style.EMPTY.withItalic(true).withColor(TextColor.YELLOW)))))
            }
        }
        return Component.literal("{$content}")
            .withStyle(Style.EMPTY.withColor(TextColor.DARK_RED).withItalic(true)
                .withHoverEvent(HoverEvent.ShowText(Component.translatable("gui.${ToBaseMod.modid}.research.500")
                    .withStyle(Style.EMPTY.withItalic(true).withColor(TextColor.RED)))))
    }

    /**
     * Private function that builds the label of a [WikiArticle] referral/link template JSON token.
     *
     * @param template The JSON template object.
     * @param article The [WikiArticle] object.
     * @param language The [WikiLanguage] for translation.
     * @return The [Component] object representing the referral title.
     * @since 0.1.0
     */
    private fun getReferralTitle(template: JsonObject, article: WikiArticle, language: WikiLanguage): Component {
        val text = template.get("text")
        if (text != null) {
            return Component.literal(text.asString)
        }
        if (template.getBool("plural") == true) {
            val phrase = language.toString(article.title)
            return Component.literal(language.pluralise(phrase))
        }
        if (template.getBool("short") == true) {
            return article.short
        }
        return article.title
    }

}
