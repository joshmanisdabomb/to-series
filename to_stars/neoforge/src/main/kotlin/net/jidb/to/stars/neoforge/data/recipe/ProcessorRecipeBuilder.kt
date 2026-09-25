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

/**
 * Builds a processor recipe and, alongside it, the loot table its results are actually drawn from.
 *
 * A result is described twice over: once in the loot table, which is what the machine runs, and once in the preview, which is what the recipe book shows.
 * The preview is a list of frames, one per possible outcome, that the book cycles through, so a recipe with a chance in it shows both what it usually gives and what it sometimes gives instead.
 *
 * @param registry The item registry that an ingredient named by tag is resolved against.
 */
class ProcessorRecipeBuilder(private val registry: HolderLookup.RegistryLookup<Item>) : RecipeBuilder {

    /**
     * The section of the recipe book the unlock advancement is filed under.
     */
    private var category = RecipeCategory.MISC

    /**
     * The section of the recipe book the recipe itself is filed under.
     */
    private var book = ProcessorRecipeBookCategory.MISC

    /**
     * The kind of machine that runs the recipe, or `null` where none has been set yet.
     */
    private var type: ProcessorType? = null

    /**
     * The lowest tier of machine that will run the recipe, or `null` where any tier will.
     */
    private var tier: MachineTier? = null

    /**
     * What goes into the recipe, and how many of each.
     */
    private val ingredients = mutableListOf<ProcessorRecipeIngredient>()

    /**
     * What the recipe book shows, as one frame per possible outcome, which it cycles through.
     */
    private var preview = mutableListOf<MutableList<Optional<ItemStackTemplate>>>(mutableListOf())

    /**
     * The loot table the results are actually drawn from.
     */
    private var output: LootTable.Builder = LootTable.Builder()

    /**
     * Adjustments applied to every pool added to the loot table, such as a fortune bonus.
     */
    private val poolModifiers = mutableListOf<LootPool.Builder.(LootPoolEntryContainer.Builder<*>) -> LootPool.Builder>()

    /**
     * How long the recipe takes, in ticks.
     */
    private var time: Int = 800

    /**
     * How much energy the recipe costs.
     */
    private var energy: Long = 0L

    /**
     * The advancement that unlocks the recipe.
     */
    private val advancement = RecipeUnlockAdvancementBuilder()

    /**
     * The recipes this one is grouped with in the book, or `null` where it stands alone.
     */
    private var group: String? = null

    /**
     * Adds an ingredient satisfied by anything in a tag.
     *
     * @param tag The tag that satisfies it.
     * @param count How many of it the recipe wants. Defaults to `1`.
     * @return This builder, so that the call can be chained.
     */
    fun requires(tag: TagKey<Item>, count: Int = 1) = requires(Ingredient.of(registry.getOrThrow(tag)), count)

    /**
     * Adds an ingredient satisfied by one particular item.
     *
     * @param item The item that satisfies it.
     * @param count How many of it the recipe wants. Defaults to `1`.
     * @return This builder, so that the call can be chained.
     */
    fun requires(item: ItemLike, count: Int = 1) = requires(Ingredient.of(item.asItem()), count)

    /**
     * Adds an ingredient.
     *
     * @param ingredient What satisfies it.
     * @param count How many of it the recipe wants. Defaults to `1`.
     * @return This builder, so that the call can be chained.
     */
    fun requires(ingredient: Ingredient, count: Int = 1): ProcessorRecipeBuilder {
        ingredients.add(ProcessorRecipeIngredient(ingredient, count))
        return this
    }

    /**
     * Sets which kind of machine runs the recipe.
     *
     * @param type The kind of machine.
     * @return This builder, so that the call can be chained.
     */
    fun requiresType(type: ProcessorType): ProcessorRecipeBuilder {
        this.type = type
        return this
    }

    /**
     * Sets the lowest tier of machine that will run the recipe.
     *
     * @param tier The lowest tier, or `null` to let any tier run it.
     * @return This builder, so that the call can be chained.
     */
    fun requiresTier(tier: MachineTier?): ProcessorRecipeBuilder {
        this.tier = tier
        return this
    }

    /**
     * Adds a result the recipe always gives, in a fixed amount.
     *
     * @param item The item to give.
     * @param count How many of it. Defaults to `1`.
     * @return This builder, so that the call can be chained.
     */
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

    /**
     * Adds a result the recipe gives some of the time, in a fixed amount.
     *
     * @param item The item to give.
     * @param count How many of it. Defaults to `1`.
     * @param chance How often it is given, from `0` to `1`. Defaults to `1`.
     * @return This builder, so that the call can be chained.
     */
    fun outputChance(item: Item, count: Int = 1, chance: Float = 1f) = outputChance(item, count, count, chance)

    /**
     * Adds a result the recipe gives some of the time, in a range of amounts, optionally giving something else the rest of the time.
     *
     * The preview is multiplied out across every amount either result can come to, so that the recipe book cycles through all of them.
     *
     * @param item The item to give.
     * @param min The fewest of it to give. Defaults to `1`.
     * @param max The most of it to give. Defaults to `1`.
     * @param chance How often it is given, from `0` to `1`. Defaults to `1`.
     * @param otherwise The item to give the rest of the time, or `null` to give nothing. Defaults to `null`.
     * @param otherwiseMin The fewest of that to give. Defaults to `1`.
     * @param otherwiseMax The most of that to give. Defaults to `1`.
     * @return This builder, so that the call can be chained.
     */
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

    /**
     * Clears the preview, for a recipe whose results the book should be told about by hand rather than worked out from what has been added.
     *
     * @return This builder, so that the call can be chained.
     */
    fun clearPreviewFrames(): ProcessorRecipeBuilder {
        preview.clear()
        return this
    }

    /**
     * Adds or replaces one frame of the preview, i.e. one of the outcomes the recipe book cycles through.
     *
     * @param display What that frame shows, or `null` for an empty one. Defaults to `null`.
     * @param index Which frame to replace, or `null` to add a new one on the end. Defaults to `null`.
     * @return This builder, so that the call can be chained.
     */
    fun previewFrame(display: List<Optional<ItemStackTemplate>>? = null, index: Int? = null): ProcessorRecipeBuilder {
        if (display == null) {
            if (index != null) preview.removeAt(index)
        } else {
            if (index != null) preview[index] = display.toMutableList()
            else preview.add(display.toMutableList())
        }
        return this
    }

    /**
     * Adds an adjustment applied to every pool of the loot table, such as a fortune bonus.
     *
     * @param modify The adjustment to apply.
     * @return This builder, so that the call can be chained.
     */
    fun modifyLootPool(modify: LootPool.Builder.(LootPoolEntryContainer.Builder<*>) -> LootPool.Builder): ProcessorRecipeBuilder {
        poolModifiers.add(modify)
        return this
    }

    /**
     * Sets how long the recipe takes.
     *
     * @param time How long, in ticks.
     * @return This builder, so that the call can be chained.
     */
    fun time(time: Int): ProcessorRecipeBuilder {
        this.time = time
        return this
    }

    /**
     * Sets how much energy the recipe costs.
     *
     * @param energy How much energy.
     * @return This builder, so that the call can be chained.
     */
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

    /**
     * The loot table the recipe's results are drawn from, which is written alongside the recipe itself.
     *
     * @return The loot table builder.
     */
    fun getLootTable() = output

}
