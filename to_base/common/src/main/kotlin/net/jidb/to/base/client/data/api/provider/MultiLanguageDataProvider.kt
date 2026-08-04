package net.jidb.to.base.client.data.api.provider

import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

/**
 * A [DataProvider] writing out every locale of a mod at once, rather than one provider per language as vanilla's own does.
 * It starts from the translations gathered by the collections and can be extended to add more by hand, by overriding [addTranslations].
 *
 * A locale with nothing in it is skipped, so declaring one and then never adding to it leaves no empty file behind.
 *
 * @param tokens The translations to start from, keyed by locale and then by translation key. Defaults to none.
 * @property output Where the generated files are written.
 * @property modid The mod ID the files are written under.
 * @since 0.3.0
 */
open class MultiLanguageDataProvider(tokens: Map<String, Map<String, Component>> = emptyMap(), protected val output: PackOutput, protected val modid: String) : DataProvider {

    /**
     * The translations written out when the provider runs, keyed by locale and then by translation key.
     *
     * @since 0.3.0
     */
    protected val tokens = tokens.mapValues { it.value.toMutableMap() }.toMutableMap()

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        addTranslations()
        val root = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid)
        val futures = tokens.mapNotNull { (locale, tokens) ->
            if (tokens.isEmpty()) return@mapNotNull null
            val json = codec.encode(tokens, JsonOps.INSTANCE, JsonObject()).getOrThrow()
            val target = root.resolve("lang").resolve("$locale.json")
            return DataProvider.saveStable(cache, json, target)
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    override fun getName() = "Multi-Locale Languages, all locales for mod: $modid"

    /**
     * Called before anything is written, for a subclass to add the translations that no collection produced.
     *
     * @return [Unit]
     * @since 0.3.0
     */
    open fun addTranslations() = Unit

    /**
     * Adds a translation, replacing whatever was already written under that key for the locale.
     *
     * @param key The translation key.
     * @param value The translation.
     * @param locale The locale to write it for. Defaults to [DEFAULT_LOCALE].
     * @since 0.3.0
     */
    fun add(key: String, value: Component, locale: String = DEFAULT_LOCALE) {
        tokens.getOrPut(locale) { mutableMapOf() }[key] = value
    }

    /**
     * Adds a translation written as plain text.
     *
     * @param key The translation key.
     * @param value The translation.
     * @param locale The locale to write it for. Defaults to [DEFAULT_LOCALE].
     * @return [Unit]
     * @since 0.3.0
     */
    fun add(key: String, value: String, locale: String = DEFAULT_LOCALE) = add(key, Component.literal(value), locale)

    /**
     * Adds the display name of a block.
     *
     * @param block The block being named.
     * @param value The display name.
     * @param locale The locale to write it for. Defaults to [DEFAULT_LOCALE].
     * @return [Unit]
     * @since 0.3.0
     */
    fun add(block: Block, value: String, locale: String = DEFAULT_LOCALE) = add(block.descriptionId, value, locale)

    /**
     * Adds the display name of an item.
     *
     * @param item The item being named.
     * @param value The display name.
     * @param locale The locale to write it for. Defaults to [DEFAULT_LOCALE].
     * @return [Unit]
     * @since 0.3.0
     */
    fun add(item: Item, value: String, locale: String = DEFAULT_LOCALE) = add(item.descriptionId, value, locale)

    /**
     * Adds the display name of a mob effect.
     *
     * @param effect The effect being named.
     * @param value The display name.
     * @param locale The locale to write it for. Defaults to [DEFAULT_LOCALE].
     * @return [Unit]
     * @since 0.3.0
     */
    fun add(effect: MobEffect, value: String, locale: String = DEFAULT_LOCALE) = add(effect.descriptionId, value, locale)

    /**
     * Adds the display name of an entity type.
     *
     * @param entity The entity type being named.
     * @param value The display name.
     * @param locale The locale to write it for. Defaults to [DEFAULT_LOCALE].
     * @return [Unit]
     * @since 0.3.0
     */
    fun add(entity: EntityType<*>, value: String, locale: String = DEFAULT_LOCALE) = add(entity.descriptionId, value, locale)

    /**
     * Adds the display name of a tag, under the key vanilla reads a tag's name from.
     *
     * @param tag The tag being named.
     * @param value The display name.
     * @param locale The locale to write it for. Defaults to [DEFAULT_LOCALE].
     * @since 0.3.0
     */
    fun add(tag: TagKey<*>, value: String, locale: String = DEFAULT_LOCALE) {
        val registry = tag.registry().identifier().toShortLanguageKey().replace('/', '.')
        val identifier = tag.location().toShortLanguageKey().replace('/', '.')
        add("tag.$registry.$identifier", value, locale)
    }

    companion object {

        /**
         * The locale a translation is written for where none is named, i.e. American English.
         *
         * @since 0.3.0
         */
        const val DEFAULT_LOCALE = "en_us"

        /**
         * The codec each locale's file is written through, mapping a translation key to its translation.
         *
         * @since 0.3.0
         */
        protected val codec = Codec.unboundedMap(Codec.STRING, ComponentSerialization.CODEC)

    }

}
