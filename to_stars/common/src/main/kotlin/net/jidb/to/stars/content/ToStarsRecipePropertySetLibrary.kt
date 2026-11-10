package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.RecipePropertySetLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.info.ProcessorType
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.jidb.to.stars.recipe.processor.ProcessorRecipeIngredient

object ToStarsRecipePropertySetLibrary : RecipePropertySetLibrary(ToStarsMod.modid) {

    val centrifuge by this { if (it.type == ToStarsMod.recipeTypes.processor && (it as? ProcessorRecipe)?.type == ProcessorType.CENTRIFUGE) it.ingredients.map(ProcessorRecipeIngredient::ingredient) else emptyList() }

}
