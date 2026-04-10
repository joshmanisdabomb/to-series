package net.jidb.to.base.client.data.collection.module.lang

import net.jidb.to.base.client.data.collection.event.LangClientDataCollectionEvent
import net.jidb.to.base.client.data.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.collection.DataCollection
import net.minecraft.network.chat.Component

open class SimpleLanguageClientDataCollectionModule(private val value: String, private val lang: String = "en_us") : ClientDataCollectionModule() {

    override fun generateLang(collection: DataCollection<*>, event: LangClientDataCollectionEvent): Map<String, Map<String, Component>>? {
        val key = collection.entry
        return mapOf(lang to mapOf("${key.registry().path}.${key.identifier().namespace}.${key.identifier().path}" to Component.literal(value)))
    }

}