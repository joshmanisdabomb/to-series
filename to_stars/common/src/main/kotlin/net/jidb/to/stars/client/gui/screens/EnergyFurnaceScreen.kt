package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.client.pub.gui.components.ProcessBarWidget
import net.jidb.to.base.pub.inventory.ToEnergyItemContainerListener
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.gui.components.AbstractProcessorComponents
import net.jidb.to.stars.client.gui.components.EnergyFurnaceRecipeBookComponent
import net.jidb.to.stars.info.EnergyFurnaceType
import net.jidb.to.stars.inventory.menu.AbstractProcessorMenu
import net.jidb.to.stars.inventory.menu.EnergyFurnaceMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.navigation.ScreenPosition
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeBookCategories

/**
 * The screen for an energy furnace that runs smelting recipes as a processor. Contains a furnace recipe book.
 *
 * It shows how much energy the machine has, how far through its recipe it is and how efficient it has become.
 * Opening or closing the recipe book shifts each of those across, since the whole interface slides over to make room.
 *
 * @param M The type of the interface being drawn.
 * @param menu The interface being drawn.
 * @param filterName What the recipe book's filter button says it filters by.
 * @param tabInfos The tabs the recipe book is divided into.
 * @param playerInventory The inventory of the player who opened it.
 * @param title The title of the interface.
 */
class EnergyFurnaceScreen(menu: EnergyFurnaceMenu, playerInventory: Inventory, title: Component) : AbstractRecipeBookScreen<EnergyFurnaceMenu>(menu, EnergyFurnaceRecipeBookComponent(menu, getRecipeFilterName(menu), getRecipeTabInfos(menu)), playerInventory, title) {

    /**
     * Watches the battery slots so that a battery's own charge can be shown in its tooltip.
     */
    protected val energyItemListener = ToEnergyItemContainerListener(menu.batterySlots, minecraft::level)

    protected var components = AbstractProcessorComponents(this, menu, energyItemListener)
        .setDefaults(::addRenderableWidget, { leftPos }, { topPos }, { customHeight })
        .setProgressBar { addRenderableWidget(ProcessBarWidget({
            (AbstractProcessorMenu.dataSchema.getShortValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.PROGRESS) ?: 0) / (AbstractProcessorMenu.dataSchema.getShortValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.MAX_PROGRESS) ?: 0).toFloat()
        }, leftPos + 80, topPos + 49)) }

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

        val extracted = AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_EXTRACT_CHANGE) ?: 0
        if (extracted > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, litProgress, leftPos + 35, topPos + 50, 14, 14)
        }
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

    companion object {

        val customHeight = 182

        /**
         * The path for the texture resource used in rendering the GUI of the energy furnace screen.
         */
        val texture = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/gui/energy_furnace.png")

        /**
         * Identifier representing the burn progress bar texture location used to represent fuel "progress" (always solid in an energy furnace while smelting).
         * Just points to the same texture used in a vanilla furnace.
         */
        val litProgress = Identifier.withDefaultNamespace("container/furnace/lit_progress")

        fun getRecipeFilterName(menu: EnergyFurnaceMenu) = when (menu.getClientData()?.type) {
            EnergyFurnaceType.SMOKER -> Component.translatable("gui.recipebook.toggleRecipes.smokable")
            EnergyFurnaceType.BLAST_FURNACE -> Component.translatable("gui.recipebook.toggleRecipes.blastable")
            EnergyFurnaceType.KILN -> Component.translatable("gui.${ToStarsMod.modid}.kiln.recipebook.filter")
            else -> Component.translatable("gui.recipebook.toggleRecipes.smeltable")
        }

        fun getRecipeTabInfos(menu: EnergyFurnaceMenu) = when (menu.getClientData()?.type) {
            EnergyFurnaceType.SMOKER -> listOf(
                RecipeBookComponent.TabInfo(SearchRecipeBookCategory.SMOKER),
                RecipeBookComponent.TabInfo(Items.PORKCHOP, RecipeBookCategories.SMOKER_FOOD)
            )
            EnergyFurnaceType.BLAST_FURNACE -> listOf(
                RecipeBookComponent.TabInfo(SearchRecipeBookCategory.BLAST_FURNACE),
                RecipeBookComponent.TabInfo(Items.REDSTONE_ORE, RecipeBookCategories.BLAST_FURNACE_BLOCKS),
                RecipeBookComponent.TabInfo(Items.IRON_SHOVEL, Items.GOLDEN_LEGGINGS, RecipeBookCategories.BLAST_FURNACE_MISC)
            )
            EnergyFurnaceType.KILN -> KilnScreen.createTabInfos()
            else -> listOf(
                RecipeBookComponent.TabInfo(SearchRecipeBookCategory.FURNACE),
                RecipeBookComponent.TabInfo(Items.PORKCHOP, RecipeBookCategories.FURNACE_FOOD),
                RecipeBookComponent.TabInfo(Items.STONE, RecipeBookCategories.FURNACE_BLOCKS),
                RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.EMERALD, RecipeBookCategories.FURNACE_MISC)
            )
        }

    }

}
