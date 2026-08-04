package net.jidb.to.base.api.wiki.language

import net.jidb.to.base.pub.wiki.language.EnglishWikiLanguage
import net.minecraft.network.chat.Component

/**
 * Interface representing a language used for wiki components, providing functionality for localization and text generation.
 *
 * @since 0.1.0
 */
interface WikiLanguage {

    /**
     * The locale that this language object represents, e.g. "en_us".
     *
     * @since 0.1.0
     */
    val locale: String

    /**
     * Converts the given [Component] into its string representation.
     *
     * @param component The [Component] to be converted into a string.
     * @return The string representation of the provided [Component].
     * @since 0.1.0
     */
    fun toString(component: Component): String = component.string

    /**
     * Converts a given phrase into its plural form according to the rules of the language.
     *
     * @param phrase The phrase that needs to be pluralized.
     * @return The pluralized form of the given phrase.
     * @since 0.1.0
     */
    fun pluralise(phrase: String): String

    /**
     * Constructs a noun representation for a given resource registry, optionally pluralized and/or prefixed with an article (e.g. "a" or "an").
     *
     * @param registry The resource registry key to be converted into a noun.
     * @param plural Indicates whether the resulting noun should be in its plural form. Defaults to `false`.
     * @param article Indicates whether the resulting noun should be prefixed with an article ("a" or "an") based on the rules of the language. Defaults to `false`.
     * @return The formatted resource noun, potentially pluralized and/or prefixed with an article.
     * @since 0.1.0
     */
    fun getResourceNoun(registry: String, plural: Boolean = false, article: Boolean = false): String

    /**
     * Generates an introduction string based on the provided parameters, adapting to singular or plural forms and optionally referencing past events or changes.
     *
     * @param self The subject or entity for which the introduction is being generated.
     * @param description An optional description providing additional details about the subject.
     * @param about An optional parameter that offers context about what the introduction pertains to.
     * @param plural A boolean indicating whether the introduction should use a pluralized form.
     * @param past A boolean indicating whether the introduction refers to past events or states.
     * @param introVer String reference for the initial version related to the subject.
     * @param reintroVer An optional string reference for a reintroduced version, if applicable.
     * @param removeVer An optional string reference for a removed version, if applicable.
     * @return A formatted introduction string tailored to the provided parameters.
     * @since 0.1.0
     */
    fun generateIntroduction(self: String, description: String?, about: String?, plural: Boolean, past: Boolean, introVer: String, reintroVer: String? = null, removeVer: String? = null): String

    /**
     * Generates an introduction string for a specific mod version, incorporating details such as
     * the mod name, version ordinal, additional information, and references to previous or next versions.
     *
     * @param self The name of the entity or subject for which the introduction is being generated.
     * @param mod The name of the mod associated with the version.
     * @param ordinal The ordinal or version identifier (e.g., "first", "second") describing the mod version.
     * @param extra Additional context or details to include in the introduction.
     * @param previous An optional reference to the preceding version, if one exists.
     * @param next An optional reference to the succeeding version, if one exists.
     * @return A formatted string that introduces the specified mod version and associated details.
     * @since 0.1.0
     */
    fun generateModVersionIntroduction(self: String, mod: String, ordinal: String, extra: String, previous: String?, next: String?): String

    /**
     * Generates a formatted string representation for the most recent mod version.
     *
     * @param version The mod version string to process and format.
     * @return A string representation of the provided version, formatted to represent the most recent mod version.
     * @since 0.1.0
     */
    fun generateModRecentVersion(version: String): String

    companion object {

        /**
         * A global list that stores instances of [WikiLanguage].
         *
         * This list is pre-populated with an instance of [EnglishWikiLanguage] configured for the "en_us" locale.
         *
         * @since 0.1.0
         */
        //TODO this should be a registry
        val languages: MutableList<WikiLanguage> = mutableListOf(EnglishWikiLanguage("en_us"))

    }

}
