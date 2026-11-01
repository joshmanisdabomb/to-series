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
open class SmithingTemplateLanguageClientDataCollectionModule(private val prompt: String? = null, private val applies: String? = null, private val base: String? = null, private val ingredients: String? = null, private val description: String? = null, private val title: String = "Smithing Template", private val lang: String = "en_us") : ClientDataCollectionModule() {

    override fun generateLang(collection: DataCollection<*>, event: LangClientDataCollectionEvent): Map<String, Map<String, Component>>? {
        val key = collection.entry
        val name = key.identifier().path.replace("_smithing_template", "")
        val tokens = mutableMapOf(
            "${key.registry().path}.${key.identifier().namespace}.${key.identifier().path}" to Component.literal(title),
            "${key.registry().path}.${key.identifier().namespace}.${key.identifier().path}.new" to Component.literal(description ?: IdentifierLanguageClientDataCollectionModule.defaultInflector(name)),
        )

        if (prompt != null) {
            tokens["${key.registry().path}.${key.identifier().namespace}.smithing_template.$name.additions_slot_description"] = Component.literal(prompt)
        }
        if (applies != null) {
            tokens["${key.registry().path}.${key.identifier().namespace}.smithing_template.$name.applies_to"] = Component.literal(applies)
        }
        if (base != null) {
            tokens["${key.registry().path}.${key.identifier().namespace}.smithing_template.$name.base_slot_description"] = Component.literal(base)
        }
        if (ingredients != null) {
            tokens["${key.registry().path}.${key.identifier().namespace}.smithing_template.$name.ingredients"] = Component.literal(ingredients)
        }

        return mapOf(lang to tokens)
    }

}
