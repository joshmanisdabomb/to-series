package net.jidb.to.base.wiki

import com.google.gson.JsonObject
import net.jidb.to.base.helper.RegistryHelper
import net.jidb.to.base.helper.addString
import net.jidb.to.base.helper.getString

class EnglishWikiLanguage(override val locale: String) : WikiLanguage {

    override fun getResourceNoun(registry: String, plural: Boolean, article: Boolean): String {
        val ret = StringBuilder(registry.replace('_', ' '))

        if (article) {
            ret.insert(0, when (ret) {
                else -> when (ret[0].lowercaseChar()) {
                    'a', 'e', 'i', 'o', 'u' -> "an "
                    else -> "a "
                }
            })
        }
        if (plural) ret.append("s")

        return ret.toString()
    }

    override fun generateIntroduction(self: String, description: String?, about: String?, plural: Boolean, past: Boolean, introVer: String, reintroVer: String?, removeVer: String?): String {
        val string = StringBuilder()
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
            string
                .append(if (removeVer != null) ", " else " and ")
                .append("reintroduced in ")
                .append(reintroVer)
        }
        if (removeVer != null) {
            string
                .append(" and removed in ")
                .append(removeVer)
        }

        return string.toString()
    }

}
