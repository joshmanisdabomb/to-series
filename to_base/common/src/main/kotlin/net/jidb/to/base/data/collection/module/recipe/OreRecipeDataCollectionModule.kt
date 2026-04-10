package net.jidb.to.base.data.collection.module.recipe

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.collection.module.DataCollectionModule
import net.jidb.to.base.helper.IdentifierHelper.identifier
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import kotlin.jvm.optionals.getOrNull

open class OreRecipeDataCollectionModule(protected val result: ItemLike? = null, protected val experience: Float = 0f, protected val group: ((name: String) -> String)? = { it }, protected val category: RecipeCategory = RecipeCategory.MISC, protected val modify: SimpleCookingRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> SimpleCookingRecipeBuilder = { _, _ -> this }) : DataCollectionModule() {

    override fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): Boolean {
        val modid = collection.entry.identifier().namespace
        val name = collection.entry.identifier().path
            .replace("_ore", "")
            .replace("deepslate_", "")
            .replace("nether_", "")

        val raw = result ?:
            BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, name)).getOrNull()?.value() ?:
            BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, "raw_$name")).getOrNull()?.value() ?:
            BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, "${name}_dust")).getOrNull()?.value() ?:
            BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, "${name}_ingot")).getOrNull()?.value()
        if (raw == null) return false

        val group = group?.invoke(raw.asItem().identifier.toString())
        modify(SimpleCookingRecipeBuilder
            .blasting(Ingredient.of(collection.`object`), category, raw, experience, 100), collection, event)
            .group(group)
            .apply { event.helper.createHas(this, collection.`object`) }
            .save(event.output, ResourceKey.create(Registries.RECIPE, raw.asItem().identifier.withSuffix("_from_blasting_${collection.entry.identifier().path}")))
        modify(SimpleCookingRecipeBuilder
            .smelting(Ingredient.of(collection.`object`), category, raw, experience, 200), collection, event)
            .group(group)
            .apply { event.helper.createHas(this, collection.`object`) }
            .save(event.output, ResourceKey.create(Registries.RECIPE, raw.asItem().identifier.withSuffix("_from_smelting_${collection.entry.identifier().path}")))

        return true
    }

}