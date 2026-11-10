package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.pub.inventory.ToEnergyItemContainerListener
import net.jidb.to.stars.inventory.menu.AbstractProcessorMenu
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe

interface IProcessorScreen {

    val processorScreen: AbstractContainerScreen<out AbstractProcessorMenu>
    val processorMenu: AbstractProcessorMenu

    /**
     * Watches the battery slots so that a battery's own charge can be shown in its tooltip.
     */
    val energyListener: ToEnergyItemContainerListener?

    val processorLeftPos: Int
    val processorTopPos: Int
    val processorImageHeight: Int

    fun <T> processorAddWidget(widget: T): T where T : GuiEventListener, T : Renderable, T : NarratableEntry

    fun getRecipeTranslation(recipe: ResourceKey<Recipe<*>>, icon: ResourceKey<Item>? = null): Component = Component.translatable("recipe.${recipe.identifier().toLanguageKey()}")

}
