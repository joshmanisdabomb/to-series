package net.jidb.to.base.api.transfer.item

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import java.util.Objects

/**
 * Represents a distinct item resource with its own [item] type and [components]. Basically an [ItemStack] that can be compared with each other.
 * Used as the resource type for [ItemTransferContext].
 *
 * @property item the [Item] type of the resource
 * @property components the [DataComponentPatch] that is applied to an [ItemStack].
 * @since 0.6.0
 */
data class ItemResource(val item: Item, val components: DataComponentPatch) {

    /**
     * Constructs an [ItemResource] with the [Item] and [components] in the given [ItemStack].
     * @param stack A stack containing an item and component patch.
     * @since 0.6.0
     */
    constructor(stack: ItemStack) : this(stack.item, stack.componentsPatch)

    /**
     * Constructs an [ItemResource] with the [ItemLike] and default empty [components].
     * @param item The [Item] like object.
     * @since 0.6.0
     */
    constructor(item: ItemLike) : this(item, DataComponentPatch.EMPTY)

    /**
     * Constructs an [ItemResource] with the [ItemLike] and given [components].
     * @param item The [Item] like object.
     * @param components the [DataComponentPatch] that is applied to an [ItemStack].
     * @since 0.6.0
     */
    constructor(item: ItemLike, components: DataComponentPatch) : this(item.asItem(), DataComponentPatch.EMPTY)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemResource) return false

        return hashCode() == other.hashCode() && item === other.item && components == other.components
    }

    override fun hashCode() = Objects.hash(item, components)

    companion object {

        /**
         * An [ItemResource] object representing an empty stack with no contents.
         *
         * @since 0.6.0
         */
        val empty = ItemResource(Items.AIR)

    }

}
