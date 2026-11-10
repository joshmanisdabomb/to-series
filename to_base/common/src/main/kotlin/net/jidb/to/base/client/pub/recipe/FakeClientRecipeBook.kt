package net.jidb.to.base.client.pub.recipe

import net.minecraft.client.ClientRecipeBook
import net.minecraft.stats.RecipeBookSettings
import net.minecraft.world.inventory.RecipeBookType

/**
 * A stand-in for the player's own recipe book, which keeps whether this book is open and filtered without touching what vanilla keeps for its own.
 *
 * @since 1.1.0
 */
class FakeClientRecipeBook : ClientRecipeBook() {

    /**
     * Whether the book is open.
     */
    var open = false

    /**
     * Whether the book is only showing what can currently be made.
     */
    var filtering = false

    /**
     * The settings vanilla reads off a recipe book, kept here so that nothing is written to the player's own.
     */
    var settings = RecipeBookSettings()

    override fun isOpen(recipeBookType: RecipeBookType) = open
    override fun setOpen(recipeBookType: RecipeBookType, open: Boolean) {
        this.open = open
        settings.setOpen(RecipeBookType.FURNACE, open)
    }

    override fun isFiltering(recipeBookType: RecipeBookType) = filtering
    override fun setFiltering(recipeBookType: RecipeBookType, filtering: Boolean) {
        this.filtering = filtering
        settings.setFiltering(RecipeBookType.FURNACE, filtering)
    }

    override fun getBookSettings() = settings
    override fun setBookSetting(bookType: RecipeBookType, open: Boolean, filtering: Boolean) {
        settings.setOpen(RecipeBookType.FURNACE, open)
        settings.setFiltering(RecipeBookType.FURNACE, filtering)
    }
    override fun setBookSettings(settings: RecipeBookSettings) {
        this.settings = settings
    }

}
