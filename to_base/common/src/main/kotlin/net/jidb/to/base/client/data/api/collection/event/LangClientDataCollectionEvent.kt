package net.jidb.to.base.client.data.api.collection.event

import net.minecraft.network.chat.Component

/**
 * [net.jidb.to.base.data.api.collection.event.DataCollectionEvent] gathering the translations of a mod, keyed by locale and then by translation key.
 * Every module contributing to a locale is merged into the same map, so the translations of one locale can be spread over as many modules as suit.
 *
 * @since 0.3.0
 */
class LangClientDataCollectionEvent : net.jidb.to.base.data.api.collection.event.DataCollectionEvent<Map<String, Map<String, Component>>, Map<String, Map<String, Component>>, Map<String, Map<String, Component>>>() {

    override fun combineFromModules(results: Iterable<Map<String, Map<String, Component>>>): Map<String, Map<String, Component>>? {
        if (results.count() <= 0) return null
        val tokens = mutableMapOf<String, MutableMap<String, Component>>()
        results.forEach {
            it.forEach { (locale, localeTokens) ->
                tokens.getOrPut(locale) { mutableMapOf() } += localeTokens
            }
        }
        return tokens
    }

    override fun combineFromCollections(results: Iterable<Map<String, Map<String, Component>>>): Map<String, Map<String, Component>>? = combineFromModules(results)

}
