package net.jidb.to.base.api.wiki

import com.google.gson.JsonObject
import net.jidb.to.base.api.helper.DATA_GSON
import net.jidb.to.base.api.wiki.language.WikiLanguage

abstract class WikiArticleTokenParser<R, F> {

    open fun start() = Unit

    abstract fun append(append: R)

    abstract fun build(): F

    fun parse(content: String, language: WikiLanguage): F {
        start()
        val matches = JSON_REGEX.findAll(content)
        for (match in matches) {
            if (match.value.startsWith("{{")) {
                append(consumeToken(DATA_GSON.fromJson(match.value.substring(1, match.value.length - 1), JsonObject::class.java), language))
            } else {
                append(consumeText(match.value, language))
            }
        }
        return build()
    }

    abstract fun consumeText(content: String, language: WikiLanguage): R

    abstract fun consumeToken(content: JsonObject, language: WikiLanguage): R

    companion object {
        private val JSON_REGEX = "\\{[^{}]*(?:\\{[^{}]*\\}[^{}]*)*\\}|[^{}]+".toRegex()
    }

}