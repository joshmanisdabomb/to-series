package net.jidb.to.base.api.transfer.item

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import java.util.*

data class ItemResource(val item: Item, val components: DataComponentPatch) {

    constructor(stack: ItemStack) : this(stack.item, stack.componentsPatch)
    constructor(item: ItemLike) : this(item, DataComponentPatch.EMPTY)
    constructor(item: ItemLike, components: DataComponentPatch) : this(item.asItem(), DataComponentPatch.EMPTY)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemResource) return false

        return hashCode() == other.hashCode() && item === other.item && components == other.components
    }

    override fun hashCode() = Objects.hash(item, components)

    companion object {
        val empty = ItemResource(Items.AIR)
    }

}
