package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.pub.inventory.ToEnergyItemContainerListener
import net.jidb.to.stars.client.gui.components.AbstractProcessorComponents
import net.jidb.to.stars.client.gui.components.ProcessorRecipeBookComponent
import net.jidb.to.stars.inventory.menu.ProcessorMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.navigation.ScreenPosition
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack

/**
 * The screen a machine that runs processor recipes is opened into, alongside the recipe book of that machine's recipes.
 *
 * It shows how much energy the machine has by it, how far through its recipe it is and how efficient it has become; opening or closing the recipe book shifts each of those across, since the whole interface slides over to make room.
 *
 * @param M The type of the interface being drawn.
 * @param menu The interface being drawn.
 * @param filterName What the recipe book's filter button says it filters by.
 * @param tabInfos The tabs the recipe book is divided into.
 * @param playerInventory The inventory of the player who opened it.
 * @param title The title of the interface.
 * @property customHeight How tall the background is, which is larger than the height vanilla assumes.
 */
abstract class ProcessorScreen<M : ProcessorMenu>(menu: M, filterName: Component, tabInfos: List<RecipeBookComponent.TabInfo>, playerInventory: Inventory, title: Component, val customHeight: Int) : AbstractRecipeBookScreen<M>(menu, ProcessorRecipeBookComponent(menu, filterName, tabInfos), playerInventory, title) {

    /**
     * The background of the screen.
     */
    protected abstract val texture: Identifier

    /**
     * Watches the battery slots so that a battery's own charge can be shown in its tooltip.
     */
    protected val energyItemListener = ToEnergyItemContainerListener(menu.batterySlots, minecraft::level)

    protected var components = AbstractProcessorComponents(this, menu, energyItemListener)
        .setDefaults(::addRenderableWidget, { leftPos }, { topPos }, { customHeight })

    init {
        inventoryLabelY = customHeight - 94
    }

    override fun init() {
        super.init()
        topPos = (height - customHeight) / 2

        components.init()
    }

    override fun getRecipeBookButtonPosition() = ScreenPosition(leftPos + imageWidth - 26, (height - customHeight).div(2) + 17)

    override fun onRecipeBookButtonClick() {
        super.onRecipeBookButtonClick()
        components.onRecipeBookButtonClick()
    }

    override fun containerTick() {
        components.containerTick()
    }

    override fun hasClickedOutside(mx: Double, my: Double, xo: Int, yo: Int): Boolean {
        val clickedOutside = mx < xo.toDouble() || my < yo.toDouble() || mx >= (xo + imageWidth).toDouble() || my >= (yo + customHeight).toDouble()
        return components.recipeBook?.hasClickedOutside(mx, my, leftPos, topPos, imageWidth, customHeight) == true && clickedOutside
    }

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick)
        val i = leftPos
        val j = (height - customHeight) / 2
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, i, j, 0.0f, 0.0f, imageWidth, customHeight, 256, 256)

        components.extractIcons(graphics)
    }

    override fun extractTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        super.extractTooltip(graphics, mouseX, mouseY)
        components.extractTooltip(graphics, mouseX, mouseY)
    }

    override fun getTooltipFromContainerItem(stack: ItemStack): List<Component> {
        run {
            val tooltip = energyItemListener.getTooltipFromContainerItem(stack, minecraft.level ?: return@run, minecraft.player ?: return@run, hoveredSlot?.index ?: return@run, minecraft.hasShiftDown(), minecraft.options.advancedItemTooltips)
            if (tooltip != null) {
                return tooltip
            }
        }
        return super.getTooltipFromContainerItem(stack)
    }

}
