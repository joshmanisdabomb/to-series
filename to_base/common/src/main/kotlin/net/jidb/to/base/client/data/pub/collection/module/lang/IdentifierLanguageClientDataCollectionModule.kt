package net.jidb.to.base.client.data.pub.collection.module.lang

import net.jidb.to.base.client.data.api.collection.event.LangClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey

open class IdentifierLanguageClientDataCollectionModule() : ClientDataCollectionModule() {

    override fun generateLang(collection: DataCollection<*>, event: LangClientDataCollectionEvent): Map<String, Map<String, Component>>? {
        val translationKey = getTranslationKey(collection.entry)
        val name = inflector(getEntryName(collection.entry))
        return mapOf("en_us" to mapOf(translationKey to Component.literal(name)))
    }

    protected open fun getTranslationKey(key: ResourceKey<*>): String = "${key.registry().path}.${key.identifier().namespace}.${key.identifier().path}"

    protected open fun getEntryName(key: ResourceKey<*>): String = key.identifier().path

    protected open fun inflector(name: String) = name
        .replace('_', ' ')
        .split(' ')
        .joinToString(" ") {
            it.replaceFirstChar { it.uppercase() }
        }

}