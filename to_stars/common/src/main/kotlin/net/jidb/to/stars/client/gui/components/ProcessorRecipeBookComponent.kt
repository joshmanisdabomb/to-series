package net.jidb.to.stars.client.gui.components

import net.jidb.to.base.mixin.client.GhostSlotsAccessor
import net.jidb.to.base.mixin.client.RecipeBookComponentAccessor
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.ProcessorMenu
import net.jidb.to.stars.recipe.processor.ProcessorRecipeDisplay
import net.minecraft.client.ClientRecipeBook
import net.minecraft.client.gui.components.WidgetSprites
import net.minecraft.client.gui.screens.recipebook.GhostSlots
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.client.gui.screens.recipebook.RecipeCollection
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.stats.RecipeBookSettings
import net.minecraft.util.context.ContextMap
import net.minecraft.world.entity.player.StackedItemContents
import net.minecraft.world.inventory.RecipeBookType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.crafting.display.RecipeDisplay

class ProcessorRecipeBookComponent(menu: ProcessorMenu, val filterName: Component, tabInfos: List<TabInfo>) : RecipeBookComponent<ProcessorMenu>(menu, tabInfos) {

    init {
        (this as RecipeBookComponentAccessor).`to_base$setBook`(FakeRecipeBook())
    }

    override fun isCraftingSlot(slot: Slot) = slot.index in menu.inputSlots || slot.index in menu.outputSlots

    override fun selectMatchingRecipes(collection: RecipeCollection, stackedContents: StackedItemContents) {
        collection.selectRecipes(stackedContents) { it is ProcessorRecipeDisplay }
    }

    override fun fillGhostRecipe(ghosts: GhostSlots, recipe: RecipeDisplay, context: ContextMap) {
        if (recipe is ProcessorRecipeDisplay) {
            for ((i, display) in recipe.input.withIndex()) {
                println(display)
                val slot = menu.inputSlots.getOrNull(i) ?: continue
                (ghosts as GhostSlotsAccessor).`to_base$setInput`(menu.getSlot(slot), context, display)
            }
            for ((i, display) in recipe.output.withIndex()) {
                println(display)
                val slot = menu.outputSlots.getOrNull(i) ?: continue
                (ghosts as GhostSlotsAccessor).`to_base$setInput`(menu.getSlot(slot), context, display)
            }
        }
    }

    override fun sendUpdateSettings() = Unit

    override fun getFilterButtonTextures() = filterSprites

    override fun getRecipeFilterName() = filterName

    companion object {

        private val filterSprites = WidgetSprites(
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "processor/filter_enabled"),
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "processor/filter_disabled"),
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "processor/filter_enabled_highlighted"),
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "processor/filter_disabled_highlighted")
        )

    }

    class FakeRecipeBook : ClientRecipeBook() {

        var open = false

        var filtering = false

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

}
