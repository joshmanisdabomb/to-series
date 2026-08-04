package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import kotlin.jvm.optionals.getOrNull

/**
 * A [DataCollectionModule] generating the smelting and blasting recipes of an ore.
 *
 * What the ore smelts into is found from the name rather than named: the `_ore` suffix and any `deepslate_` or `nether_` prefix are stripped, and the item left over is looked for as it is, then with a `raw_` prefix, then with a `_dust` or `_ingot` suffix.
 * Nothing is generated where no such item exists.
 *
 * @property result The item the ore smelts into, or `null` to find it from the name. Defaults to `null`.
 * @property experience How much experience smelting the ore awards. Defaults to none.
 * @property group A function producing the recipe book group from the result's name, or `null` for no group. Defaults to the name itself.
 * @property category The recipe book category the recipes appear under. Defaults to miscellaneous.
 * @property cookingCategory The cooking book category the recipes appear under. Defaults to miscellaneous.
 * @property modify A function adjusting each recipe, given the collection and event being generated for. Defaults to leaving it as it is.
 * @since 0.3.0
 */
open class OreRecipeDataCollectionModule(protected val result: ItemLike? = null, protected val experience: Float = 0f, protected val group: ((name: String) -> String)? = { it }, protected val category: RecipeCategory = RecipeCategory.MISC, protected val cookingCategory: CookingBookCategory = CookingBookCategory.MISC, protected val modify: SimpleCookingRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> SimpleCookingRecipeBuilder = { _, _ -> this }) : DataCollectionModule() {

    override fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): Boolean {
        val modid = collection.entry.identifier().namespace
        val name = collection.entry.identifier().path
            .replace("_ore", "")
            .replace("deepslate_", "")
            .replace("nether_", "")

        val raw = result
            ?: BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, name)).getOrNull()?.value()
            ?: BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, "raw_$name")).getOrNull()?.value()
            ?: BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, "${name}_dust")).getOrNull()?.value()
            ?: BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, "${name}_ingot")).getOrNull()?.value()
        if (raw == null) return false

        val group = group?.invoke(raw.asItem().identifier.toString())
        modify(SimpleCookingRecipeBuilder
            .blasting(Ingredient.of(collection.`object`), category, cookingCategory, raw, experience, 100), collection, event)
            .group(group)
            .apply { event.helper.createHas(this, collection.`object`) }
            .save(event.output, ResourceKey.create(Registries.RECIPE, raw.asItem().identifier.withSuffix("_from_blasting_${collection.entry.identifier().path}")))
        modify(SimpleCookingRecipeBuilder
            .smelting(Ingredient.of(collection.`object`), category, cookingCategory, raw, experience, 200), collection, event)
            .group(group)
            .apply { event.helper.createHas(this, collection.`object`) }
            .save(event.output, ResourceKey.create(Registries.RECIPE, raw.asItem().identifier.withSuffix("_from_smelting_${collection.entry.identifier().path}")))

        return true
    }

}
