package net.jidb.to.stars.recipe.processor

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient

/**
 * One ingredient of a processor recipe, together with how many of it the recipe wants, which vanilla's own ingredient has no room for.
 *
 * @property ingredient What will satisfy the ingredient.
 * @property count How many of it the recipe wants. Defaults to `1`.
 */
data class ProcessorRecipeIngredient(val ingredient: Ingredient, val count: Int = 1) {

    companion object {

        /**
         * The codec an ingredient is read from a recipe file through.
         */
        val codec = RecordCodecBuilder.create {
            it.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ProcessorRecipeIngredient::ingredient),
                Codec.INT.fieldOf("count").forGetter(ProcessorRecipeIngredient::count),
            )
                .apply(it, ::ProcessorRecipeIngredient)
        }

        /**
         * The codec an ingredient is sent to the client through.
         */
        val streamCodec = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            ProcessorRecipeIngredient::ingredient,
            ByteBufCodecs.INT,
            ProcessorRecipeIngredient::count,
            ::ProcessorRecipeIngredient
        )

    }

}
