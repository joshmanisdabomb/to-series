package net.jidb.to.base.api.wiki.language

import net.jidb.to.base.pub.wiki.language.EnglishWikiLanguage
import net.minecraft.network.chat.Component

interface WikiLanguage {

    val locale: String

    fun toString(component: Component): String = component.string

    fun pluralise(phrase: String): String

    fun getResourceNoun(registry: String, plural: Boolean = false, article: Boolean = false): String

    fun generateIntroduction(self: String, description: String?, about: String?, plural: Boolean, past: Boolean, introVer: String, reintroVer: String? = null, removeVer: String? = null): String
    fun generateModVersionIntroduction(self: String, mod: String, ordinal: String, extra: String, previous: String?, next: String?): String
    fun generateModRecentVersion(version: String): String

    companion object {
        val languages: MutableList<WikiLanguage> = mutableListOf(EnglishWikiLanguage("en_us"))
    }

}