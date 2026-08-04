package net.jidb.to.base.data.api

import net.jidb.to.base.api.helper.RegistryHelper
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentInitializers
import net.minecraft.core.component.DataComponentType
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item

/**
 * A record of the data components each item was registered with, kept so that a generator can ask what an item's default component value is.
 * Vanilla builds an item's components into an immutable map that no longer says where each value came from, and a delayed or holder component cannot be read at all until the registries exist, so the values are recorded here as they are declared instead.
 *
 * @since 0.6.0
 */
object ToDataItemHelper {

    /**
     * The properties each item was registered with, which is what its components are indexed by.
     *
     * @since 0.6.0
     */
    private val properties = mutableMapOf<Item, Item.Properties>()

    /**
     * The components declared with a value already in hand.
     *
     * @since 0.6.0
     */
    private val components = mutableMapOf<Item.Properties, MutableList<ComponentEntry<*>>>()

    /**
     * The components declared as a function of the registries, which cannot be read until those exist.
     *
     * @since 0.6.0
     */
    private val delayedComponents = mutableMapOf<Item.Properties, MutableList<DelayedComponentEntry<*>>>()

    /**
     * The components declared as a registry key, whose value is whatever that key resolves to.
     *
     * @since 0.6.0
     */
    private val holderComponents = mutableMapOf<Item.Properties, MutableList<HolderComponentEntry<*>>>()

    /**
     * A component declared with a value already in hand.
     *
     * @param T The type of the component's value.
     * @property type The type of the component.
     * @property value The value it was declared with.
     * @since 0.6.0
     */
    private data class ComponentEntry<T : Any>(val type: DataComponentType<T>, val value: T)

    /**
     * A component declared as a function of the registries, resolved only once those exist.
     *
     * @param T The type of the component's value.
     * @property type The type of the component.
     * @property value The initializer producing its value.
     * @since 0.6.0
     */
    private data class DelayedComponentEntry<T : Any>(val type: DataComponentType<T>, val value: DataComponentInitializers.SingleComponentInitializer<T>)

    /**
     * A component declared as a registry key, whose value is whatever that key resolves to.
     *
     * @param T The type the key points at.
     * @property type The type of the component.
     * @property value The key its value is resolved from.
     * @since 0.6.0
     */
    private data class HolderComponentEntry<T : Any>(val type: DataComponentType<Holder<T>>, val value: ResourceKey<T>)

    /**
     * Records which properties an item was registered with.
     *
     * @param item The item being registered.
     * @param properties The properties it is being registered with.
     * @since 0.6.0
     */
    fun add(item: Item, properties: Item.Properties) {
        this.properties[item] = properties
    }

    /**
     * Records a component declared with a value already in hand.
     *
     * @param T The type of the component's value.
     * @param properties The properties the component is being declared on.
     * @param type The type of the component.
     * @param value The value it is being declared with.
     * @since 0.6.0
     */
    fun <T : Any> addComponent(properties: Item.Properties, type: DataComponentType<T>, value: T) {
        components.getOrPut(properties) { mutableListOf() }.add(ComponentEntry(type, value))
    }

    /**
     * Records a component declared as a function of the registries.
     *
     * @param T The type of the component's value.
     * @param properties The properties the component is being declared on.
     * @param type The type of the component.
     * @param value The initializer producing its value.
     * @since 0.6.0
     */
    fun <T : Any> addDelayedComponent(properties: Item.Properties, type: DataComponentType<T>, value: DataComponentInitializers.SingleComponentInitializer<T>) {
        delayedComponents.getOrPut(properties) { mutableListOf() }.add(DelayedComponentEntry(type, value))
    }

    /**
     * Records a component declared as a registry key.
     *
     * @param T The type the key points at.
     * @param properties The properties the component is being declared on.
     * @param type The type of the component.
     * @param value The key its value is resolved from.
     * @since 0.6.0
     */
    fun <T : Any> addHolderComponent(properties: Item.Properties, type: DataComponentType<Holder<T>>, value: ResourceKey<T>) {
        holderComponents.getOrPut(properties) { mutableListOf() }.add(HolderComponentEntry(type, value))
    }

    /**
     * The default value of one of an item's components, whichever of the three ways it was declared.
     * A directly declared value is preferred, then a delayed one, then a holder, matching the order they would take effect in.
     *
     * @param T The type of the component's value.
     * @param item The item to read the component of.
     * @param type The type of the component to read.
     * @param lookup The registries a delayed component is resolved against, which may be `null` where no delayed component is expected. Defaults to `null`.
     * @return The component's default value, or `null` where the item does not carry it.
     * @throws NullPointerException If the component was declared as a delayed one and no lookup was given.
     * @since 0.6.0
     */
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
