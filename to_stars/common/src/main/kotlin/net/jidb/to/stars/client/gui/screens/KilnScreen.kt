package net.jidb.to.stars.client.gui.screens

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.KilnMenu
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import java.util.Optional

/**
 * Represents the GUI for the [KilnMenu], extending [AbstractFurnaceScreen].
 * Mostly a boilerplate subclass that provides kiln recipe book categories and filter localised text.
 *
 * @constructor Initializes a new [KilnScreen] using the provided menu instance, player inventory, and the screen title.
 * @param menu The KilnMenu instance that manages the state and behavior of the container.
 * @param inventory The player's inventory presented within this screen.
 * @param title The title component displayed at the top of the screen.
 * @since 0.2.0
 * @see KilnMenu
 */
class KilnScreen(menu: KilnMenu, inventory: Inventory, title: Component) : AbstractFurnaceScreen<KilnMenu>(menu, inventory, title, Component.translatable("gui.${ToStarsMod.modid}.kiln.recipebook.filter"), texture, litProgress, burnProgress, listOf(
    RecipeBookComponent.TabInfo(ItemStack(Items.COMPASS), Optional.empty(), ToStarsMod.recipeCustomCategories.kiln_all),
    RecipeBookComponent.TabInfo(ItemStack(Items.STONE), Optional.empty(), ToStarsMod.recipeCustomCategories.kiln_blocks),
    RecipeBookComponent.TabInfo(ItemStack(Items.LAVA_BUCKET), Optional.of(ItemStack(Items.EMERALD)), ToStarsMod.recipeCustomCategories.kiln_misc)
)) {

    companion object {

        /**
         * The path for the texture resource used in rendering the GUI of the kiln screen.
         * Just points to the default vanilla furnace GUI.
         */
        val texture = Identifier.withDefaultNamespace("textures/gui/container/furnace.png")

        /**
         * Identifier representing the progress bar texture location used to indicate the remaining duration of the current item's smelt time.
         * Just points to the same texture used in a vanilla furnace.
         */
        val burnProgress = Identifier.withDefaultNamespace("container/furnace/burn_progress")

        /**
         * Identifier representing the burn progress bar texture location used to indicate the remaining duration of the current fuel's burn time.
         * Just points to the same texture used in a vanilla furnace.
         */
        val litProgress = Identifier.withDefaultNamespace("container/furnace/lit_progress")

    }

}
