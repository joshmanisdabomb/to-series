package net.jidb.to.stars.recipe.processor

import net.jidb.to.stars.ToStarsMod
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.crafting.RecipeBookCategory

enum class ProcessorRecipeBookCategory(private val category: () -> RecipeBookCategory) : StringRepresentable {

    MISC({ ToStarsMod.recipeCategories.processor_misc });

    fun get() = category()

    override fun getSerializedName() = name.lowercase()

    companion object {

        val codec = StringRepresentable.fromEnum(::values)

        val byId = ByIdMap.continuous(ProcessorRecipeBookCategory::ordinal, entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)

        val streamCodec = ByteBufCodecs.idMapper(byId, ProcessorRecipeBookCategory::ordinal)

    }

}
