package net.jidb.to.base.data.api

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.minecraft.advancements.predicates.ItemPredicate
import net.minecraft.advancements.triggers.CriteriaTriggers
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.advancements.triggers.InventoryChangeTrigger
import net.minecraft.core.HolderGetter
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.Optional

/**
 * A helper that builds the pieces a generated recipe usually needs.
 * At present that is the unlock criterion, which every recipe has to declare and which vanilla only offers one ingredient at a time.
 *
 * @property registry The item registry the ingredients are looked up in.
 * @since 0.3.0
 */
class ToDataRecipeHelper(val registry: HolderGetter<Item>) {

    /**
     * @since 1.1.0
     */
    fun createHas(unlockedBy: (name: String, trigger: Criterion<InventoryChangeTrigger.TriggerInstance>) -> Unit, vararg item: ItemLike) = unlockedBy(
        "has_${item.joinToString("_or_") { it.asItem().identifier.path }}",
        CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
            InventoryChangeTrigger.TriggerInstance(
                Optional.empty(),
                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                item.map { ItemPredicate.Builder.item().of(registry, it.asItem()).build() }
            )
        )
    )

    /**
     * @since 1.1.0
     */
    fun createHas(unlockedBy: (name: String, trigger: Criterion<InventoryChangeTrigger.TriggerInstance>) -> Unit, vararg tag: TagKey<Item>) = unlockedBy(
        "has_${tag.joinToString("_or_") { it.location.path }}",
        CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
            InventoryChangeTrigger.TriggerInstance(
                Optional.empty(),
                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                tag.map { ItemPredicate.Builder.item().of(registry, it).build() }
            )
        )
    )

    /**
     * Unlocks a recipe once the player has obtained any of the given items.
     * The criterion is named after every item it covers, so that two recipes unlocked by different ingredients do not collide.
     *
     * @param builder The recipe being built.
     * @param item The items any of which unlock the recipe.
     * @since 0.3.0
     */
    fun createHas(builder: RecipeBuilder, vararg item: ItemLike) = createHas(builder::unlockedBy, *item)

    /**
     * Unlocks a recipe once the player has obtained an item from any of the given tags.
     * The criterion is named after every tag it covers, so that two recipes unlocked by different ingredients do not collide.
     *
     * @param builder The recipe being built.
     * @param tag The item tags any of which unlock the recipe.
     * @since 0.3.0
     */
    fun createHas(builder: RecipeBuilder, vararg tag: TagKey<Item>) = createHas(builder::unlockedBy, *tag)

    /**
     * @since 1.1.0
     */
    fun createHas(builder: SmithingTransformRecipeBuilder, vararg item: ItemLike) = createHas(builder::unlocks, *item)

    /**
     * @since 1.1.0
     */
    fun createHas(builder: SmithingTransformRecipeBuilder, vararg tag: TagKey<Item>) = createHas(builder::unlocks, *tag)

    /**
     * @since 1.1.0
     */
    fun createHas(builder: SmithingTrimRecipeBuilder, vararg item: ItemLike) = createHas(builder::unlocks, *item)

    /**
     * @since 1.1.0
     */
    fun createHas(builder: SmithingTrimRecipeBuilder, vararg tag: TagKey<Item>) = createHas(builder::unlocks, *tag)

}
