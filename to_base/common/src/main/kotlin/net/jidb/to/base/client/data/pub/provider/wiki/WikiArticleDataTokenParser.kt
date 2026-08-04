package net.jidb.to.base.client.data.pub.provider.wiki

import com.google.gson.JsonObject
import net.jidb.to.base.api.helper.JsonHelper.addBool
import net.jidb.to.base.api.helper.JsonHelper.addString
import net.jidb.to.base.api.helper.JsonHelper.getString
import net.jidb.to.base.api.wiki.WikiArticleTokenParser
import net.jidb.to.base.api.wiki.language.WikiLanguage

/**
 * The [WikiArticleTokenParser] used while an article is baked, which resolves the templates an author writes into the tokens the game reads back at runtime.
 *
 * A reference is left as the token it was written as, because what it should say depends on a registry that is not loaded during generation; only the templates that depend on the article's own metadata, such as its introduction, are worked out here.
 *
 * @param data The metadata of the article being baked, i.e. its `index.json` and changelog.
 * @since 0.1.0
 */
class WikiArticleDataTokenParser(protected val data: JsonObject) : WikiArticleTokenParser<String, String>() {

    /**
     * The text built up so far, which is what one pass of the parser produces.
     *
     * @since 0.1.0
     */
    private var builder = StringBuilder()

    /**
     * The changelog of the article's subject, in the order the versions were released.
     *
     * @since 0.1.0
     */
    val changelog by lazy { data.getAsJsonArray("changelog").filterIsInstance<JsonObject>() }

    override fun start() {
        builder = StringBuilder()
    }

    override fun append(append: String) {
        builder.append(append)
    }

    override fun build() = builder.toString()

    override fun consumeText(content: String, language: WikiLanguage) = content

    override fun consumeToken(content: JsonObject, language: WikiLanguage): String {
        val template = content.get("template")?.asString
        return when (template) {
            "introduction" -> consumeIntroductionToken(content, language)
            "introduction_version" -> consumeModVersionIntroductionToken(content, language)
            else -> consumeReferralToken(content)
        }
    }

    /**
     * Resolves the `introduction` template, i.e. the opening sentence naming what the article is about and which versions it existed in.
     *
     * The changelog decides how it reads: the first entry is the version the subject was added in, an entry from a different mod to the one it started in is where it was carried over, and a last entry of `removed` puts the whole sentence in the past tense.
     *
     * @param content The token as it was written.
     * @param language The language the article is being baked for.
     * @return The generated sentence.
     * @since 0.1.0
     */
    private fun consumeIntroductionToken(content: JsonObject, language: WikiLanguage): String {
        val last = changelog.last()
        val introduction = changelog.first()
        val reintroduction = changelog.find {
            val versionMod = it.getString("version")!!.substringBefore(':')
            val lastMod = last.getString("version")!!.substringBefore(':')
            val introMod = introduction.getString("version")!!.substringBefore(':')
            versionMod != introMod && versionMod == lastMod
        }

        val removed = last.takeIf { it.getString("type") == "removed" }
        val past = content.get("past")?.asBoolean ?: (removed != null)

        val plural = content.get("plural")?.asBoolean ?: false
        content.addBool("plural", plural)
        content.addString("of", "self")
        content.addString("template", "referral")

        val self = consumeReferralToken(content)
        val introVer = consumeReferralToken(JsonObject().apply {
            addString("template", "referral")
            addString("of", "to_base:mod_version / ${introduction.getString("version")}")
        })
        val reintroVer = if (reintroduction == null) null else consumeReferralToken(JsonObject().apply {
            addString("template", "referral")
            addString("of", "to_base:mod_version / ${reintroduction.getString("version")}")
        })
        val removeVer = if (removed == null) null else consumeReferralToken(JsonObject().apply {
            addString("template", "referral")
            addString("of", "to_base:mod_version / ${removed.getString("version")}")
        })

        return language.generateIntroduction(self, content.getString("description"), data.getAsJsonArray("about").first().asString, plural, past, introVer, reintroVer, removeVer)
    }

    /**
     * Resolves the `introduction_version` template, i.e. the opening sentence of a version's changelog page, which names the mod it belongs to and the releases either side of it.
     *
     * @param content The token as it was written.
     * @param language The language the article is being baked for.
     * @return The generated sentence.
     * @since 0.1.0
     */
    private fun consumeModVersionIntroductionToken(content: JsonObject, language: WikiLanguage): String {
        content.addString("of", "self")
        content.addString("template", "referral")

        val self = consumeReferralToken(content)
        val mod = data.getString("parent")!!.let { consumeReferralToken(JsonObject().apply {
            addString("template", "referral")
            addString("of", it)
        }) }

        val previous = data.getString("previous")?.let { consumeReferralToken(JsonObject().apply {
            addString("template", "referral")
            addString("of", it)
            addBool("short", true)
        }) }
        val next = data.getString("next")?.let { consumeReferralToken(JsonObject().apply {
            addString("template", "referral")
            addString("of", it)
            addBool("short", true)
        }) }

        return language.generateModVersionIntroduction(self, mod, content.getString("ordinal")!!, content.getString("extra") ?: "", previous, next)
    }

    /**
     * Resolves a reference to another article, which is left as the token it was written as so that the game can name its subject from the registry once that has loaded.
     *
     * @param content The token as it was written.
     * @return The token, written back out.
     * @since 0.1.0
     */
    private fun consumeReferralToken(content: JsonObject) = "{$content}"

}
