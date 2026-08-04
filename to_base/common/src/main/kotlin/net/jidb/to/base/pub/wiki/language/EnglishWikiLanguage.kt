package net.jidb.to.base.pub.wiki.language

import net.jidb.to.base.api.helper.RegistryHelper
import net.jidb.to.base.api.wiki.language.WikiLanguage
import org.modeshape.common.text.Inflector

/**
 * The [WikiLanguage] for English, which builds the sentences a wiki article generates rather than writes out by hand.
 * Pluralisation is handed to an inflector rather than guessed at, so an irregular noun in a registry name reads correctly.
 *
 * @property locale The locale code this language is used for.
 * @since 0.1.0
 */
class EnglishWikiLanguage(override val locale: String) : WikiLanguage {

    override fun pluralise(phrase: String) = inflector.pluralize(phrase)

    override fun getResourceNoun(registry: String, plural: Boolean, article: Boolean): String {
        val noun = registry.replace('_', ' ')
        var ret = if (plural) pluralise(noun) else noun

        if (article) {
            ret = when (noun) {
                else -> when (noun[0].lowercaseChar()) {
                    'a', 'e', 'i', 'o', 'u' -> "an "
                    else -> "a "
                }
            } + ret
        }

        return ret
    }

    override fun generateIntroduction(self: String, description: String?, about: String?, plural: Boolean, past: Boolean, introVer: String, reintroVer: String?, removeVer: String?): String {
        val builder = StringBuilder()
            .append(self)
            .append(when {
                plural && past -> " were "
                past -> " was "
                plural -> " are "
                else -> " is "
            })
            .append(description ?: about?.let { getResourceNoun(RegistryHelper.splitResourceKey(it).first.path, plural, true) } ?: (if (plural) "things" else "a thing"))
            .append(" introduced in ")
            .append(introVer)

        if (reintroVer != null) {
            builder
                .append(if (removeVer != null) ", " else " and ")
                .append("reintroduced in ")
                .append(reintroVer)
        }
        if (removeVer != null) {
            builder
                .append(" and removed in ")
                .append(removeVer)
        }

        return builder.toString()
    }

    override fun generateModVersionIntroduction(self: String, mod: String, ordinal: String, extra: String, previous: String?, next: String?): String {
        val builder = StringBuilder()
            .append(self)
            .append(" is the ")
            .append(ordinal)
            .append(" release of ")
            .append(mod)

        if (extra.isNotEmpty()) {
            builder.append(extra)
        }

        if (previous != null || next != null) {
            builder.append(". ")
            if (previous != null) {
                builder.append("The previous release was ")
                builder.append(previous)
            }
            if (next != null) {
                if (previous != null) {
                    builder.append(", and the ")
                } else {
                    builder.append("The ")
                }
                builder.append("following release is ")
                builder.append(next)
            }
        }

        builder.append("{{\"template\": \"version_release\"}}")

        return builder.toString()
    }

    override fun generateModRecentVersion(version: String) = StringBuilder()
        .append(version)
        .toString()

    companion object {

        /**
         * The inflector that pluralises a noun, which knows the irregular forms a naive rule would get wrong.
         *
         * @since 0.1.0
         */
        private val inflector = Inflector.getInstance()

    }

}
