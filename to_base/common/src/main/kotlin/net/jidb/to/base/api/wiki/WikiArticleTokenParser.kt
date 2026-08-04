package net.jidb.to.base.api.wiki

import com.google.gson.JsonObject
import net.jidb.to.base.api.helper.JsonHelper
import net.jidb.to.base.api.wiki.language.WikiLanguage

/**
 * An abstract inline JSON token parser for handling display-related content in wiki articles.
 * This class is responsible for interpreting either tokens in between double braces `{{}}`, or simple text, and converting them into fragments of type [R].
 * The [R] fragments should then have been built into a final output [F].
 *
 * @param R The type of fragments to be produced for each text or token during parsing.
 * @param F The type of final output produced by the parser.
 * @since 0.1.0
 */
abstract class WikiArticleTokenParser<R, F> {

    /**
     * A method that runs when [parse] has first been called, to mark when the parser has begun parsing a piece of [String] content.
     *
     * This method is intended to be overridden by subclasses to perform any necessary setup or initialization before the parsing process begins.
     *
     * @since 0.1.0
     */
    open fun start() = Unit

    /**
     * Appends a parsed result to the internal state during [parse].
     *
     * The results of [consumeText] or [consumeToken] is passed here, to be aggregated into the desired output structure [F] to be returned in [build].]
     *
     * @param append The parsed result to append.
     * @since 0.1.0
     */
    abstract fun append(append: R)

    /**
     * Builds and returns the final output of type [F] after previous calls to [append].
     *
     * This method is expected to be called after the parsing process is complete for a [String], and it provides the accumulated structure or result derived from the parsed content.
     *
     * @return The final constructed result of type [F].
     * @since 0.1.0
     */
    abstract fun build(): F

    /**
     * The parse routine to transform the inline JSON tokens in the given [String] content and render it all into a final output [F].
     *
     * The method identifies and processes sections of the content that represent JSON objects in double braces `{{}}` or plain text, passing them to abstract consume methods.
     *
     * @param content The string content to be parsed and processed.
     * @param language The [WikiLanguage] used for parsing and content processing.
     * @return The final result of type [F] after processing the content.
     * @since 0.1.0
     */
    fun parse(content: String, language: WikiLanguage): F {
        start()
        val matches = jsonRegex.findAll(content)
        for (match in matches) {
            if (match.value.startsWith("{{")) {
                append(consumeToken(JsonHelper.gsonBuilderData.fromJson(match.value.substring(1, match.value.length - 1), JsonObject::class.java), language))
            } else {
                append(consumeText(match.value, language))
            }
        }
        return build()
    }

    /**
     * Processes a section of simple text content within an article and returns the result of type [R].
     *
     * This method probably shouldn't do too much with [content], just convert it into desired type [R].
     *
     * @param content The text content to be processed.
     * @param language The language context in which the content is being processed.
     * @return The processed result of type [R].
     * @since 0.1.0
     */
    abstract fun consumeText(content: String, language: WikiLanguage): R

    /**
     * Consumes a detected inline JSON token from the provided content and processes it according to the language context.
     *
     * This method is intended to handle specific JSON object tokens within the parsing flow and convert them into the desired output type [R], based on the language rules or configuration.
     *
     * @param content The JSON object representing a token to be consumed and processed.
     * @param language The language context in which the token should be interpreted and transformed.
     * @return The processed result of type [R], derived from the provided content and language.
     * @since 0.1.0
     */
    abstract fun consumeToken(content: JsonObject, language: WikiLanguage): R

    companion object {

        /**
         * A regular expression used to identify and parse JSON objects or text segments in a given content string.
         *
         * This regex is designed to match JSON-like structures enclosed in curly braces (`{}`), including nested structures, as well as non-JSON text segments that do not include curly braces.
         *
         * The parsed matches can be used to distinguish between JSON tokens and simple text content, aiding in the processing and transformation of structured and unstructured data segments.
         *
         * @since 0.1.0
         */
        private val jsonRegex = "\\{[^{}]*(?:\\{[^{}]*\\}[^{}]*)*\\}|[^{}]+".toRegex()

    }

}
