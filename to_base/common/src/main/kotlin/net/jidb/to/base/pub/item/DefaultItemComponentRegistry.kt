package net.jidb.to.base.pub.item

import net.jidb.to.base.api.helper.RegistryHelper
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentInitializers
import net.minecraft.core.component.DataComponentType
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item

object DefaultItemComponentRegistry {

    private val properties = mutableMapOf<Item, Item.Properties>()
    private val components = mutableMapOf<Item.Properties, MutableList<ComponentEntry<*>>>()
    private val delayedComponents = mutableMapOf<Item.Properties, MutableList<DelayedComponentEntry<*>>>()
    private val holderComponents = mutableMapOf<Item.Properties, MutableList<HolderComponentEntry<*>>>()

    private data class ComponentEntry<T : Any>(val type: DataComponentType<T>, val value: T)
    private data class DelayedComponentEntry<T : Any>(val type: DataComponentType<T>, val value: DataComponentInitializers.SingleComponentInitializer<T>)
    private data class HolderComponentEntry<T : Any>(val type: DataComponentType<Holder<T>>, val value: ResourceKey<T>)

    fun add(item: Item, properties: Item.Properties) {
        this.properties[item] = properties
    }

    fun <T : Any> addComponent(properties: Item.Properties, type: DataComponentType<T>, value: T) {
        components.getOrPut(properties) { mutableListOf() }.add(ComponentEntry(type, value))
    }

    fun <T : Any> addDelayedComponent(properties: Item.Properties, type: DataComponentType<T>, value: DataComponentInitializers.SingleComponentInitializer<T>) {
        delayedComponents.getOrPut(properties) { mutableListOf() }.add(DelayedComponentEntry(type, value))
    }

    fun <T : Any> addHolderComponent(properties: Item.Properties, type: DataComponentType<Holder<T>>, value: ResourceKey<T>) {
        holderComponents.getOrPut(properties) { mutableListOf() }.add(HolderComponentEntry(type, value))
    }

    fun <T : Any> getDefaultComponentValue(item: Item, type: DataComponentType<T>, lookup: HolderLookup.Provider? = null): T? {
        val properties = properties[item] ?: return null

        val direct = components[properties]?.filterIsInstance<ComponentEntry<T>>()?.firstOrNull { it.type == type }?.value
        if (direct != null) {
            return direct
        }

        val delayed = delayedComponents[properties]?.filterIsInstance<DelayedComponentEntry<T>>()?.firstOrNull { it.type == type }?.value?.create(lookup!!)
        if (delayed != null) {
            return delayed
        }

        val holder = holderComponents[properties]?.filterIsInstance<HolderComponentEntry<T>>()?.firstOrNull { it.type == type }?.value
        if (holder != null) {
            return RegistryHelper.getResource(holder)
        }

        return null
    }

}
