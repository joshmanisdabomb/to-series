package net.jidb.to.base.data.pub.collection.module.recipe

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import kotlin.jvm.optionals.getOrNull

/**
 * A [DataCollectionModule] generating the recipes that pack a material into a block or nugget and unpack it again, both ways round.
 *
 * The material is found from the name rather than named: a block or nugget has its suffix stripped, and the item left over is looked for as it is, then with `_dust`, then with `_ingot`.
 * Nothing is generated where no such item exists, or where the name ends in neither suffix.
 *
 * @property result The material to pack and unpack, or `null` to find it from the name. Defaults to `null`.
 * @property blockCount How many of the material make one block. Defaults to `9`.
 * @property nuggetCount How many nuggets make one of the material. Defaults to `9`.
 * @property group A function producing the recipe book group from the material's name, or `null` for no group. Defaults to the name itself.
 * @since 0.3.0
 */
class CompactRecipeDataCollectionModule(protected val result: ItemLike? = null, protected val blockCount: Int = 9, protected val nuggetCount: Int = 9, protected val group: ((name: String) -> String)? = { it }) : DataCollectionModule() {

    override fun generateRecipes(collection: DataCollection<Item>, event: RecipeDataCollectionEvent): Boolean {
        val modid = collection.entry.identifier().namespace
        val name = collection.entry.identifier().path

        val base = name
            .replace("_block", "")
            .replace("_nugget", "")
        val resource = result
            ?: BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, base)).getOrNull()?.value()
            ?: BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, "${base}_dust")).getOrNull()?.value()
            ?: BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath(modid, "${base}_ingot")).getOrNull()?.value()
        if (resource == null) return false

        val group = group?.invoke(resource.asItem().identifier.toString())
        when {
            name.endsWith("_nugget") -> {
                getCompactRecipe(event, collection.`object`, resource, nuggetCount)
                    .group(group)
                    .apply { event.helper.createHas(this, collection.`object`) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, resource.asItem().identifier.withSuffix("_from_nuggets")))
                ShapelessRecipeBuilder.shapeless(event.registry, RecipeCategory.MISC, collection.`object`, nuggetCount)
                    .requires(resource)
                    .apply { event.helper.createHas(this, resource) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            name.endsWith("_block") -> {
                ShapelessRecipeBuilder.shapeless(event.registry, RecipeCategory.MISC, resource, blockCount)
                    .requires(collection.`object`)
                    .group(group)
                    .apply { event.helper.createHas(this, collection.`object`) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, resource.asItem().identifier))
                getCompactRecipe(event, resource, collection.`object`, blockCount)
                    .apply { event.helper.createHas(this, resource) }
                    .save(event.output, ResourceKey.create(Registries.RECIPE, collection.`object`.asItem().identifier))
            }
            else -> return false
        }
        return true
    }

    /**
     * The recipe packing a count of one item into another, shaped as a full grid where the count fills one exactly and shapeless otherwise.
     *
     * @param event The event being generated for.
     * @param input The item being packed.
     * @param output The item it packs into.
     * @param count How many of the input make one output. Defaults to `9`.
     * @return The recipe builder.
     * @since 0.3.0
     */
    private fun getCompactRecipe(event: RecipeDataCollectionEvent, input: ItemLike, output: ItemLike, count: Int = 9) = when (count) {
        9 -> ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.MISC, output)
            .define('#', input)
            .pattern("###")
            .pattern("###")
            .pattern("###")
        4 -> ShapedRecipeBuilder.shaped(event.registry, RecipeCategory.MISC, output)
            .define('#', input)
            .pattern("##")
            .pattern("##")
        else -> ShapelessRecipeBuilder.shapeless(event.registry, RecipeCategory.MISC, output)
            .requires(input, count)
    }

}
