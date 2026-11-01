package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

/**
 * A [DataCollectionModule] generating the standard vanilla recipes for equipment using a given ingot and handle ingredient.
 * Uses the item's name to determine the recipe to use.
 *
 * @property ingot The ingredient to use for the ingot of a recipe.
 * @property handle The ingredient to use for the handle of a recipe (i.e. stick).
 * @since 0.3.0
 */
class EquipmentRecipeDataCollectionModule(protected val ingot: ItemLike, protected val handle: ItemLike = Items.STICK, val modify: (name: String) -> String = { it }) : DataCollectionModule() {

    override fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): Boolean {
        val name = modify(collection.entry.identifier().path)
        when {
            name.endsWith("_sword") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.COMBAT, collection.`object`)
                    .pattern("X")
                    .pattern("X")
                    .pattern("#")
                    .define('X', ingot)
                    .define('#', handle)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_pickaxe") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.TOOLS, collection.`object`)
                    .pattern("XXX")
                    .pattern(" # ")
                    .pattern(" # ")
                    .define('X', ingot)
                    .define('#', handle)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_shovel") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.TOOLS, collection.`object`)
                    .pattern("X")
                    .pattern("#")
                    .pattern("#")
                    .define('X', ingot)
                    .define('#', handle)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_axe") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.COMBAT, collection.`object`)
                    .pattern("XX")
                    .pattern("X#")
                    .pattern(" #")
                    .define('X', ingot)
                    .define('#', handle)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_hoe") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.TOOLS, collection.`object`)
                    .pattern("XX")
                    .pattern(" #")
                    .pattern(" #")
                    .define('X', ingot)
                    .define('#', handle)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_spear") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.COMBAT, collection.`object`)
                    .pattern("  X")
                    .pattern(" # ")
                    .pattern("#  ")
                    .define('X', ingot)
                    .define('#', handle)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_helmet") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.COMBAT, collection.`object`)
                    .pattern("XXX")
                    .pattern("X X")
                    .define('X', ingot)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_chestplate") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.COMBAT, collection.`object`)
                    .pattern("X X")
                    .pattern("XXX")
                    .pattern("XXX")
                    .define('X', ingot)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_leggings") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.COMBAT, collection.`object`)
                    .pattern("XXX")
                    .pattern("X X")
                    .pattern("X X")
                    .define('X', ingot)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_boots") -> {
                ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.COMBAT, collection.`object`)
                    .pattern("X X")
                    .pattern("X X")
                    .define('X', ingot)
                    .apply { event.helper.createHas(this, ingot.asItem()) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            else -> return false
        }
        return true
    }

}
