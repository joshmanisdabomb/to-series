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

open class MultiLanguageDataProvider(tokens: Map<String, Map<String, Component>> = emptyMap(), protected val output: PackOutput, protected val modid: String) : DataProvider {

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

    override fun getName(): String {
        return "Multi-Locale Languages, all locales for mod: $modid"
    }

    open fun addTranslations() = Unit

    fun add(key: String, value: Component, locale: String = DEFAULT_LOCALE) {
        tokens.getOrPut(locale) { mutableMapOf() }[key] = value
    }
    fun add(key: String, value: String, locale: String = DEFAULT_LOCALE) = add(key, Component.literal(value), locale)

    fun add(block: Block, value: String, locale: String = DEFAULT_LOCALE) = add(block.descriptionId, value, locale)
    fun add(item: Item, value: String, locale: String = DEFAULT_LOCALE) = add(item.descriptionId, value, locale)
    fun add(effect: MobEffect, value: String, locale: String = DEFAULT_LOCALE) = add(effect.descriptionId, value, locale)
    fun add(entity: EntityType<*>, value: String, locale: String = DEFAULT_LOCALE) = add(entity.descriptionId, value, locale)

    fun add(tag: TagKey<*>, value: String, locale: String = DEFAULT_LOCALE) {
        val registry = tag.registry().identifier().toShortLanguageKey().replace('/', '.')
        val identifier = tag.location().toShortLanguageKey().replace('/', '.')
        add("tag.$registry.$identifier", value, locale)
    }

    companion object {
        const val DEFAULT_LOCALE = "en_us"
        protected val codec = Codec.unboundedMap(Codec.STRING, ComponentSerialization.CODEC);
    }

}