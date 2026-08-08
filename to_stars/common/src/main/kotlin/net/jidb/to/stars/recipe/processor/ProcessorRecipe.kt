package net.jidb.to.stars.recipe.processor

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.api.helper.KotlinHelper.times
import net.jidb.to.base.api.helper.KotlinHelper.transpose
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.ProcessorBlock
import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.info.ProcessorType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.context.ContextKeySet
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.PlacementInfo
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * A recipe run by a processor, which turns a set of ingredients into a set of results over a length of time and for a cost in energy.
 *
 * Unlike a furnace recipe the results are not written into the recipe itself but drawn from a loot table named after it, so that a recipe can produce different amounts and even different items each run.
 * What the recipe file does carry is a preview of what each output slot can produce, which is what the recipe book shows and what a machine decides where to put a result by.
 *
 * @property info What every recipe carries, i.e. whether it notifies the player on being unlocked.
 * @property book Where the recipe sits in the recipe book.
 * @property type The kind of machine that runs it.
 * @property tier The lowest tier of machine that will run it, or empty where any tier will.
 * @property ingredients What goes into it, and how many of each.
 * @property preview What each output slot can produce, as a row per possible result.
 * @property time How long the recipe takes, in ticks, before the machine's own speed applies.
 * @property energy How much energy the recipe costs, before the machine's own usage applies.
 */
class ProcessorRecipe(val info: Recipe.CommonInfo, val book: ProcessorRecipeBookInfo, val type: ProcessorType, val tier: Optional<MachineTier>, val ingredients: List<ProcessorRecipeIngredient>, val preview: List<List<Optional<ItemStackTemplate>>>, val time: Int, val energy: Long) : Recipe<ProcessorRecipeInput> {

    /**
     * The cached working out of where the ingredients go, since vanilla asks for it repeatedly.
     */
    private var placement: PlacementInfo? = null

    override fun matches(input: ProcessorRecipeInput, level: Level): Boolean {
        val block = input.machine.block as? ProcessorBlock ?: return false
        if (block.processor != type || (tier.isPresent && block.machine != tier.get())) return false
        for ((ingredient, count) in ingredients) {
            val found = input.items.filter { ingredient.test(it) }.sumOf { it.count }
            if (found < count) {
                return false
            }
        }
        return true
    }

    override fun placementInfo(): PlacementInfo {
        if (placement == null) {
            placement = PlacementInfo.createFromOptionals(ingredients.flatMap { listOf(Optional.of(it.ingredient)) * it.count })
        }
        return placement!!
    }

    /**
     * The first thing the preview says this recipe can produce, which is what vanilla uses wherever it expects a recipe to have a single result.
     *
     * @return The result, or an empty stack where the preview names nothing.
     */
    fun getFirstOutput() = preview.flatten().firstNotNullOfOrNull { it.getOrNull() }?.create() ?: ItemStack.EMPTY

    override fun assemble(input: ProcessorRecipeInput) = getFirstOutput()

    /**
     * Runs the recipe's loot table to work out what it actually produces this time, which is where the results come from rather than from the recipe file.
     *
     * @param recipeId The name of the recipe, which the loot table is named after.
     * @param entity The machine running it.
     * @param level The level it is in.
     * @param seed The randomness the outcome is decided by, kept by the machine so that reloading partway through does not change it.
     * @return What the recipe produced.
     */
    fun assembleAll(recipeId: Identifier, entity: BlockEntity, level: ServerLevel, seed: Long): List<ItemStack> {
        val key = ResourceKey.create(Registries.LOOT_TABLE, recipeId.withPrefix("processor/"))
        val table = level.server.reloadableRegistries().getLootTable(key)
        val params = LootParams.Builder(level)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(entity.blockPos))
            .withParameter(LootContextParams.BLOCK_STATE, entity.blockState)
            .withParameter(LootContextParams.BLOCK_ENTITY, entity)
            .create(paramSet)

        return table.getRandomItems(params, seed)
    }

    override fun isSpecial() = false

    override fun showNotification() = info.showNotification()

    override fun group() = book.group

    override fun recipeBookCategory() = book.category.get()

    override fun display() = listOf(ProcessorRecipeDisplay(
        ingredients.map { (ingredient, count) -> ingredient.display() },
        preview.transpose().map { SlotDisplay.Composite(it.map { if (it.isPresent) SlotDisplay.ItemStackSlotDisplay(it.get()) else SlotDisplay.Empty.INSTANCE }) },
        ToStarsMod.blocks.values.firstOrNull { it is ProcessorBlock && it.processor == type && (!tier.isPresent || it.machine >= tier.get()) }?.asItem()?.let { SlotDisplay.ItemSlotDisplay(it) } ?: SlotDisplay.Empty.INSTANCE
    ))

    override fun getType() = ToStarsMod.recipeTypes.processor

    override fun getSerializer() = ToStarsMod.recipeSerializers.processor

    companion object {

        /**
         * What the loot table of a recipe is given to work from, i.e. where the machine is, what it is and its block entity.
         */
        val paramSet = ContextKeySet.Builder().required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE).required(LootContextParams.BLOCK_ENTITY).build()

        /**
         * The codec a recipe is read from a data pack through.
         */
        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                Recipe.CommonInfo.MAP_CODEC.forGetter(ProcessorRecipe::info),
                ProcessorRecipeBookInfo.mapCodec.forGetter(ProcessorRecipe::book),
                ProcessorType.codec.fieldOf("machine").forGetter(ProcessorRecipe::type),
                MachineTier.codec.optionalFieldOf("tier").forGetter(ProcessorRecipe::tier),
                ProcessorRecipeIngredient.codec.listOf().fieldOf("ingredients").forGetter(ProcessorRecipe::ingredients),
                ExtraCodecs.optionalEmptyMap(ItemStackTemplate.CODEC).listOf().listOf().fieldOf("preview").forGetter(ProcessorRecipe::preview),
                Codec.INT.fieldOf("time").forGetter(ProcessorRecipe::time),
                Codec.LONG.fieldOf("energy").forGetter(ProcessorRecipe::energy)
            )
                .apply(it, ::ProcessorRecipe)
        }

        /**
         * The codec a recipe is sent to the client through.
         */
        val streamCodec = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            ProcessorRecipe::info,
            ProcessorRecipeBookInfo.streamCodec,
            ProcessorRecipe::book,
            ProcessorType.streamCodec,
            ProcessorRecipe::type,
            MachineTier.streamCodec.apply(ByteBufCodecs::optional),
            ProcessorRecipe::tier,
            ProcessorRecipeIngredient.streamCodec.apply(ByteBufCodecs.list()),
            ProcessorRecipe::ingredients,
            ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs::optional).apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()),
            ProcessorRecipe::preview,
            ByteBufCodecs.INT,
            ProcessorRecipe::time,
            ByteBufCodecs.LONG,
            ProcessorRecipe::energy,
            ::ProcessorRecipe
        )

    }

}
