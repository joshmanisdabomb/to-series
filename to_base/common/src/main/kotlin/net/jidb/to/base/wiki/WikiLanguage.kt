package net.jidb.to.base.wiki

interface WikiLanguage {

    val locale: String

    fun getResourceNoun(registry: String, plural: Boolean = false, article: Boolean = false): String

    fun generateIntroduction(self: String, description: String?, about: String?, plural: Boolean, past: Boolean, introVer: String, reintroVer: String? = null, removeVer: String? = null): String

}