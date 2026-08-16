package net.jidb.to.stars.recipe.processor

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient

data class ProcessorRecipeIngredient(val ingredient: Ingredient, val count: Int = 1) {

    companion object {

        val codec = RecordCodecBuilder.create {
            it.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ProcessorRecipeIngredient::ingredient),
                Codec.INT.fieldOf("count").forGetter(ProcessorRecipeIngredient::count),
            )
                .apply(it, ::ProcessorRecipeIngredient)
        }

        val streamCodec = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            ProcessorRecipeIngredient::ingredient,
            ByteBufCodecs.INT,
            ProcessorRecipeIngredient::count,
            ::ProcessorRecipeIngredient
        )

    }

}
