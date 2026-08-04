package net.jidb.to.base.client.data.pub.collection.module.lang

import net.jidb.to.base.client.data.api.collection.event.LangClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.network.chat.Component

/**
 * A [ClientDataCollectionModule] generating a translation written out by hand, for content whose display name does not follow from the name it was registered under.
 *
 * @param value The display name to generate.
 * @param lang The locale to generate it for. Defaults to `en_us`.
 * @since 0.3.0
 */
open class SimpleLanguageClientDataCollectionModule(private val value: String, private val lang: String = "en_us") : ClientDataCollectionModule() {

    override fun generateLang(collection: DataCollection<*>, event: LangClientDataCollectionEvent): Map<String, Map<String, Component>>? {
        val key = collection.entry
        return mapOf(lang to mapOf("${key.registry().path}.${key.identifier().namespace}.${key.identifier().path}" to Component.literal(value)))
    }

}
