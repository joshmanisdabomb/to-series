package net.jidb.to.base.wiki

import com.google.gson.JsonObject
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.helper.RegistryHelper
import net.jidb.to.base.helper.addBool
import net.jidb.to.base.helper.addString
import net.jidb.to.base.helper.getBool
import net.jidb.to.base.wiki.language.WikiLanguage
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.*
import net.minecraft.resources.Identifier

class WikiArticleDisplayTokenParser(val self: Identifier, protected val context: WikiArticleIndex) : WikiArticleTokenParser<MutableComponent, MutableComponent>() {

    protected var component = Component.empty()

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
                        .withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_GREEN))
                            .withHoverEvent(HoverEvent.ShowText(article.title)))
                }
                val resource = RegistryHelper.splitResourceKey(key)
                val article = context.byResource[resource]?.first()
                if (article != null) {
                    return Component.empty().append(getReferralTitle(content, article, language))
                        .withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLUE)).withUnderlined(true)
                            .withHoverEvent(HoverEvent.ShowText(article.title))
                            .withClickEvent(WikiArticleLink(article)))
                }
                return Component.literal("${resource.first.path}.${resource.second.namespace}.${resource.second.path}")
                    .withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GOLD)).withItalic(true)
                        .withHoverEvent(HoverEvent.ShowText(Component.translatable("gui.${ToBaseMod.modid}.research.404")
                            .withStyle(Style.EMPTY.withItalic(true).withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW))))))
            }
        }
        return Component.literal("{${content}}")
            .withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)).withItalic(true)
                .withHoverEvent(HoverEvent.ShowText(Component.translatable("gui.${ToBaseMod.modid}.research.500")
                    .withStyle(Style.EMPTY.withItalic(true).withColor(TextColor.fromLegacyFormat(ChatFormatting.RED))))))
    }

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