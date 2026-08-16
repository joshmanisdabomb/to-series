package net.jidb.to.base.pub.event.item

import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.ItemLike

class ModifyItemComponentEventContext(private val one: (item: ItemLike, patch: (components: DataComponentMap.Builder, item: ItemLike) -> Unit) -> Unit, private val multiple: (item: (item: ItemLike) -> Boolean, patch: (components: DataComponentMap.Builder, item: ItemLike) -> Unit) -> Unit) {

    fun modify(item: ItemLike, patch: (components: DataComponentMap.Builder, item: ItemLike) -> Unit) = one(item, patch)
    fun modify(predicate: (item: ItemLike) -> Boolean, patch: (components: DataComponentMap.Builder, item: ItemLike) -> Unit) = multiple(predicate, patch)

}
