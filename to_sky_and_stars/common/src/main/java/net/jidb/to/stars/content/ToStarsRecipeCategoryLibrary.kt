package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeBookCategory

object ToStarsRecipeCategoryLibrary : SimpleRegistryLibrary<RecipeBookCategory>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.RECIPE_BOOK_CATEGORY

    val processor_misc by this { RecipeBookCategory() }

}
