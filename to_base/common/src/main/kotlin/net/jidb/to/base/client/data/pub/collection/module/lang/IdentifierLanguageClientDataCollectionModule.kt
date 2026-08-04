package net.jidb.to.base.client.data.pub.collection.module.lang

import net.jidb.to.base.client.data.api.collection.event.LangClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey

/**
 * A [ClientDataCollectionModule] generating an English translation from the name a thing was registered under, by replacing underscores with spaces and capitalising each word of it.
 * This is the default every entry is given unless another language module is declared for it, so content whose registered name already reads correctly needs no translation writing at all.
 *
 * @property modifyId A function adjusting the registered name before it is turned into a display name. Defaults to leaving it as it is.
 * @since 0.3.0
 */
open class IdentifierLanguageClientDataCollectionModule(val modifyId: (id: String) -> String = { it }) : ClientDataCollectionModule() {

    override fun generateLang(collection: DataCollection<*>, event: LangClientDataCollectionEvent): Map<String, Map<String, Component>>? {
        val translationKey = getTranslationKey(collection.entry)
        val name = inflector(modifyId(getEntryName(collection.entry)))
        return mapOf("en_us" to mapOf(translationKey to Component.literal(name)))
    }

    /**
     * The translation key that the generated display name is written under, i.e. `block.modid.name`.
     *
     * @param key The registry entry being translated.
     * @return The translation key.
     * @since 0.3.0
     */
    protected open fun getTranslationKey(key: ResourceKey<*>): String = "${key.registry().path}.${key.identifier().namespace}.${key.identifier().path}"

    /**
     * The part of a registry entry's name that the display name is built from, before [modifyId] and [inflector] are applied to it.
     *
     * @param key The registry entry being translated.
     * @return The name to turn into a display name.
     * @since 0.3.0
     */
    protected open fun getEntryName(key: ResourceKey<*>): String = key.identifier().path

    /**
     * Turns a registered name into a display name, by replacing underscores with spaces and capitalising each word of it.
     * Words are capitalised across hyphens as well as spaces, so that `uranium-235` reads as `Uranium-235`.
     *
     * @param name The name to turn into a display name.
     * @return The display name.
     * @since 0.3.0
     */
    protected open fun inflector(name: String) = name
        .replace('_', ' ')
        .split(' ').joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
        .split('-').joinToString("-") { it.replaceFirstChar(Char::uppercase) }

}
