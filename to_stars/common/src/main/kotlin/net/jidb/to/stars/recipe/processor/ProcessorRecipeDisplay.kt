package net.jidb.to.stars.recipe.processor

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.stars.ToStarsMod
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.display.RecipeDisplay
import net.minecraft.world.item.crafting.display.SlotDisplay

class ProcessorRecipeDisplay(val input: List<SlotDisplay>, val output: List<SlotDisplay>, val station: SlotDisplay) : RecipeDisplay {

    override fun result() = output.firstOrNull() ?: SlotDisplay.Empty.INSTANCE

    override fun craftingStation() = station

    override fun type() = ToStarsMod.recipeDisplays.processor

    companion object {

        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                SlotDisplay.CODEC.listOf().fieldOf("input").forGetter(ProcessorRecipeDisplay::input),
                SlotDisplay.CODEC.listOf().fieldOf("output").forGetter(ProcessorRecipeDisplay::output),
                SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(ProcessorRecipeDisplay::station)
            )
                .apply(it, ::ProcessorRecipeDisplay)
        }

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
