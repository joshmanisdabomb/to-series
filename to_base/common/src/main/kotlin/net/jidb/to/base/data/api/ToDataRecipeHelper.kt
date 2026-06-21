package net.jidb.to.base.data.api

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
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

    fun createHas(builder: RecipeBuilder, vararg item: ItemLike) {
        builder.unlockedBy("has_${item.joinToString("_or_") { it.asItem().identifier.path }}", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
            InventoryChangeTrigger.TriggerInstance(
                Optional.empty(),
                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                item.map { ItemPredicate.Builder.item().of(registry, it.asItem()).build() }
            )
        ))
    }

    fun createHas(builder: RecipeBuilder, vararg tag: TagKey<Item>) {
        builder.unlockedBy("has_${tag.joinToString("_or_") { it.location.path }}", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
            InventoryChangeTrigger.TriggerInstance(
                Optional.empty(),
                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                tag.map { ItemPredicate.Builder.item().of(registry, it).build() }
            )
        ))
    }

}