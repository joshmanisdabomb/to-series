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

class ProcessorRecipe(val info: Recipe.CommonInfo, val book: ProcessorRecipeBookInfo, val type: ProcessorType, val tier: Optional<MachineTier>, val ingredients: List<ProcessorRecipeIngredient>, val preview: List<List<Optional<ItemStackTemplate>>>, val time: Int, val energy: Long) : Recipe<ProcessorRecipeInput> {

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

    fun getFirstOutput() = preview.flatten().firstNotNullOfOrNull { it.getOrNull() }?.create() ?: ItemStack.EMPTY

    override fun assemble(input: ProcessorRecipeInput) = getFirstOutput()

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

        val paramSet = ContextKeySet.Builder().required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE).required(LootContextParams.BLOCK_ENTITY).build()

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
