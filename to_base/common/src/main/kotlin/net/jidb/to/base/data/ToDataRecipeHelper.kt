package net.jidb.to.base.data

import net.jidb.to.base.helper.IdentifierHelper.identifier
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.criterion.InventoryChangeTrigger
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.*

class ToDataRecipeHelper(val registry: HolderGetter<Item>) {

    fun createHas(builder: RecipeBuilder, item: ItemLike) {
        val item = item.asItem()
        builder.unlockedBy("has_${item.identifier.path}", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
            InventoryChangeTrigger.TriggerInstance(
                Optional.empty(),
                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                listOf(ItemPredicate.Builder.item().of(registry, item).build())
            )
        ))
    }

    fun createHas(builder: RecipeBuilder, tag: TagKey<Item>) {
        builder.unlockedBy("has_${tag.location.path}", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
            InventoryChangeTrigger.TriggerInstance(
                Optional.empty(),
                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                listOf(ItemPredicate.Builder.item().of(registry, tag).build())
            )
        ))
    }

}