package net.jidb.to.stars.neoforge.data.recipe

import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.info.ProcessorType
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.jidb.to.stars.recipe.processor.ProcessorRecipeBookCategory
import net.jidb.to.stars.recipe.processor.ProcessorRecipeBookInfo
import net.jidb.to.stars.recipe.processor.ProcessorRecipeIngredient
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class ProcessorRecipeBuilder(private val registry: HolderLookup.RegistryLookup<Item>) : RecipeBuilder {

    private var category = RecipeCategory.MISC

    private var book = ProcessorRecipeBookCategory.MISC

    private var type: ProcessorType? = null

    private var tier: MachineTier? = null

    private val ingredients = mutableListOf<ProcessorRecipeIngredient>()

    private var preview = mutableListOf<MutableList<Optional<ItemStackTemplate>>>(mutableListOf())

    private var output: LootTable.Builder = LootTable.Builder()

    private val poolModifiers = mutableListOf<LootPool.Builder.(LootPoolEntryContainer.Builder<*>) -> LootPool.Builder>()

    private var time: Int = 800

    private var energy: Long = 0L

    private val advancement = RecipeUnlockAdvancementBuilder()

    private var group: String? = null

    fun requires(tag: TagKey<Item>, count: Int = 1) = requires(Ingredient.of(registry.getOrThrow(tag)), count)

    fun requires(item: ItemLike, count: Int = 1) = requires(Ingredient.of(item.asItem()), count)

    fun requires(ingredient: Ingredient, count: Int = 1): ProcessorRecipeBuilder {
        ingredients.add(ProcessorRecipeIngredient(ingredient, count))
        return this
    }

    fun requiresType(type: ProcessorType): ProcessorRecipeBuilder {
        this.type = type
        return this
    }

    fun requiresTier(tier: MachineTier?): ProcessorRecipeBuilder {
        this.tier = tier
        return this
    }

    fun outputConstant(item: Item, count: Int = 1): ProcessorRecipeBuilder {
        preview.forEach { it.add(Optional.of(ItemStackTemplate(item))) }

        val item = LootItem.lootTableItem(item)
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count.toFloat())))
        output.withPool(
            poolModifiers.fold(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(item)) { acc, fn -> fn(acc, item) }
        )
        return this
    }

    fun outputChance(item: Item, count: Int = 1, chance: Float = 1f) = outputChance(item, count, count, chance)

    fun outputChance(item: Item, min: Int = 1, max: Int = 1, chance: Float = 1f, otherwise: Item? = null, otherwiseMin: Int = 1, otherwiseMax: Int = 1): ProcessorRecipeBuilder {
        val range = min..max
        val otherRange = otherwiseMin..otherwiseMax
        val otherPermutations = if (chance < 1f) otherRange.map { listOf(Optional.empty(), Optional.ofNullable(if (otherwise == null) null else ItemStackTemplate(otherwise, it))) } else emptyList()
        val permutations = range.map { listOf(Optional.of(ItemStackTemplate(item, it)), Optional.empty()) } + otherPermutations

        preview = preview.flatMap { frame ->
            permutations.map { (frame + it).toMutableList() }
        }.toMutableList()

        var item: LootPoolEntryContainer.Builder<*> = LootItem.lootTableItem(item)
            .`when`(LootItemRandomChanceCondition.randomChance(chance))
            .apply(SetItemCountFunction.setCount(UniformGenerator.between(min.toFloat(), max.toFloat())))
        if (otherwise != null) {
            item = item.otherwise(LootItem.lootTableItem(otherwise)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(otherwiseMin.toFloat(), otherwiseMax.toFloat()))))
        }
        output.withPool(
            poolModifiers.fold(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(item)) { acc, fn -> fn(acc, item) }
        )
        return this
    }

    fun clearPreviewFrames(): ProcessorRecipeBuilder {
        preview.clear()
        return this
    }

    fun previewFrame(display: List<Optional<ItemStackTemplate>>? = null, index: Int? = null): ProcessorRecipeBuilder {
        if (display == null) {
            if (index != null) preview.removeAt(index)
        } else {
            if (index != null) preview[index] = display.toMutableList()
            else preview.add(display.toMutableList())
        }
        return this
    }

    fun modifyLootPool(modify: LootPool.Builder.(LootPoolEntryContainer.Builder<*>) -> LootPool.Builder): ProcessorRecipeBuilder {
        poolModifiers.add(modify)
        return this
    }

    fun time(time: Int): ProcessorRecipeBuilder {
        this.time = time
        return this
    }

    fun energy(energy: Long): ProcessorRecipeBuilder {
        this.energy = energy
        return this
    }

    override fun unlockedBy(name: String, criterion: Criterion<*>): ProcessorRecipeBuilder {
        advancement.unlockedBy(name, criterion)
        return this
    }

    override fun group(group: String?): ProcessorRecipeBuilder {
        this.group = group
        return this
    }

    override fun defaultId() = RecipeBuilder.getDefaultRecipeId(preview.flatten().firstNotNullOf { it.getOrNull() })

    override fun save(output: RecipeOutput, id: ResourceKey<Recipe<*>>) {
        val recipe = ProcessorRecipe(
            RecipeBuilder.createCraftingCommonInfo(true),
            ProcessorRecipeBookInfo(book, group ?: ""),
            type ?: error("No machine processor type given for this processor recipe."),
            Optional.ofNullable(tier),
            ingredients,
            preview,
            time,
            energy
        )
        output.accept(id, recipe, advancement.build(output, id, category))
    }

    fun getLootTable() = output

}
