package net.jidb.to.base.client.data.collection.event

import net.jidb.to.base.data.collection.event.DataCollectionEvent
import net.minecraft.network.chat.Component

class LangClientDataCollectionEvent : DataCollectionEvent<Map<String, Map<String, Component>>, Map<String, Map<String, Component>>, Map<String, Map<String, Component>>>() {

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