package net.jidb.to.stars.client.gui.screens

import net.jidb.to.stars.inventory.menu.KilnMenu
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeBookCategories
import java.util.List

class KilnScreen(menu: KilnMenu, inventory: Inventory, title: Component) : AbstractFurnaceScreen<KilnMenu>(menu, inventory, title, Component.translatable("gui.recipebook.toggleRecipes.smeltable"), texture, litProgress, burnProgress, listOf(
    RecipeBookComponent.TabInfo(SearchRecipeBookCategory.FURNACE),
    RecipeBookComponent.TabInfo(Items.STONE, RecipeBookCategories.FURNACE_BLOCKS),
    RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.EMERALD, RecipeBookCategories.FURNACE_MISC)
)) {

    companion object {

        val texture = Identifier.withDefaultNamespace("textures/gui/container/furnace.png")
        val burnProgress = Identifier.withDefaultNamespace("container/furnace/burn_progress")
        val litProgress = Identifier.withDefaultNamespace("container/furnace/lit_progress")

    }

}
