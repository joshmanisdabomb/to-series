package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.RecipeTypeLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.recipe.processor.ProcessorRecipe

object ToStarsRecipeLibrary : RecipeTypeLibrary(ToStarsMod.modid) {

    val processor by this<ProcessorRecipe>()

}
