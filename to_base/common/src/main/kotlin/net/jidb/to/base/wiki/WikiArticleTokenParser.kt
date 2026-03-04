package net.jidb.to.base.wiki

import com.google.gson.JsonObject
import net.jidb.to.base.helper.DATA_GSON

abstract class WikiArticleTokenParser(protected val data: JsonObject) {

    fun parse(content: String, language: WikiLanguage): String {
        val matches = JSON_REGEX.findAll(content)
        val builder = StringBuilder()
        for (match in matches) {
            if (match.value.startsWith("{{")) {
                builder.append(consumeToken(DATA_GSON.fromJson(match.value.substring(1, match.value.length - 1), JsonObject::class.java), language))
            } else {
                builder.append(consumeText(match.value, language))
            }
        }
        return builder.toString()
    }

    open fun consumeText(content: String, language: WikiLanguage) = content

    abstract fun consumeToken(content: JsonObject, language: WikiLanguage): String

    companion object {
        private val JSON_REGEX = "\\{[^{}]*(?:\\{[^{}]*\\}[^{}]*)*\\}|[^{}]+".toRegex()
    }

}