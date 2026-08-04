package net.jidb.to.stars.recipe.processor

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.stars.ToStarsMod
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.display.RecipeDisplay
import net.minecraft.world.item.crafting.display.SlotDisplay

/**
 * How a processor recipe is shown in the recipe book: what goes in, what comes out and which machine it is run in.
 *
 * @property input What each ingredient is drawn as.
 * @property output What each output slot can produce, drawn as one slot cycling through the possibilities.
 * @property station The machine the recipe is run in.
 */
class ProcessorRecipeDisplay(val input: List<SlotDisplay>, val output: List<SlotDisplay>, val station: SlotDisplay) : RecipeDisplay {

    override fun result() = output.firstOrNull() ?: SlotDisplay.Empty.INSTANCE

    override fun craftingStation() = station

    override fun type() = ToStarsMod.recipeDisplays.processor

    companion object {

        /**
         * The codec the display is read and written through.
         */
        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                SlotDisplay.CODEC.listOf().fieldOf("input").forGetter(ProcessorRecipeDisplay::input),
                SlotDisplay.CODEC.listOf().fieldOf("output").forGetter(ProcessorRecipeDisplay::output),
                SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(ProcessorRecipeDisplay::station)
            )
                .apply(it, ::ProcessorRecipeDisplay)
        }

        /**
         * The codec the display is sent to the client through.
         */
        val streamCodec = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ProcessorRecipeDisplay::input,
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ProcessorRecipeDisplay::output,
            SlotDisplay.STREAM_CODEC,
            ProcessorRecipeDisplay::station,
            ::ProcessorRecipeDisplay
        )

    }

}
