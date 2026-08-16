package net.jidb.to.base.client.data.pub.provider.wiki

import com.google.gson.JsonObject
import net.jidb.to.base.api.helper.JsonHelper.addBool
import net.jidb.to.base.api.helper.JsonHelper.addString
import net.jidb.to.base.api.helper.JsonHelper.getString
import net.jidb.to.base.api.wiki.WikiArticleTokenParser
import net.jidb.to.base.api.wiki.language.WikiLanguage

class WikiArticleDataTokenParser(protected val data: JsonObject) : WikiArticleTokenParser<String, String>() {

    private var builder = StringBuilder()
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

    private fun consumeReferralToken(content: JsonObject) = "{$content}"

}
