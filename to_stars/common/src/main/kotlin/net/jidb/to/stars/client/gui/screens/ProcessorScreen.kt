package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.KotlinHelper.either
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.pub.gui.components.AbstractBarWidget
import net.jidb.to.base.client.pub.gui.components.energy.AbstractEnergyBarWidget
import net.jidb.to.base.client.pub.gui.components.energy.MiniEnergyBarWidget
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.base.pub.inventory.ToEnergyItemContainerListener
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.gui.components.EfficiencyBarWidget
import net.jidb.to.stars.client.gui.components.ProcessorRecipeBookComponent
import net.jidb.to.stars.info.ToStarsTooltipEngine
import net.jidb.to.stars.inventory.menu.ProcessorMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.navigation.ScreenPosition
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

abstract class ProcessorScreen<M : ProcessorMenu>(menu: M, filterName: Component, tabInfos: List<RecipeBookComponent.TabInfo>, playerInventory: Inventory, title: Component, val customHeight: Int) : AbstractRecipeBookScreen<M>(menu, ProcessorRecipeBookComponent(menu, filterName, tabInfos), playerInventory, title) {

    protected abstract val texture: Identifier

    protected var recipeBook: ProcessorRecipeBookComponent? = null

    protected var energy: AbstractEnergyBarWidget? = null

    protected var efficiency: EfficiencyBarWidget? = null

    protected var progress: AbstractBarWidget? = null

    protected val energyItemListener = ToEnergyItemContainerListener(menu.batterySlots, minecraft::level)

    init {
        inventoryLabelY = customHeight - 94
    }

    override fun init() {
        super.init()
        topPos = (height - customHeight) / 2

        recipeBook = children().firstNotNullOf { it as? ProcessorRecipeBookComponent }

        energy = addEnergyBarWidget()
        efficiency = addEfficiencyBarWidget()

        menu.removeSlotListener(energyItemListener)
        menu.addSlotListener(energyItemListener)
    }

    protected open fun addEnergyBarWidget(x: Int = leftPos + 41, y: Int = topPos + 24): AbstractEnergyBarWidget = this.addRenderableWidget(
        MiniEnergyBarWidget(
            { ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_TOTAL) ?: 0L },
            { ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_CAPACITY) ?: 0L },
            x,
            y
        )
    )

    protected open fun addEfficiencyBarWidget(x: Int = leftPos + 135, y: Int = topPos + customHeight - 104) = this.addRenderableWidget(
        EfficiencyBarWidget(
            { (ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.COMPLETIONS) ?: 0) / (menu.clientData?.machineBonusMax?.toFloat() ?: 1f) },
            x,
            y
        )
    )

    override fun getRecipeBookButtonPosition() = ScreenPosition(leftPos + imageWidth - 26, (height - customHeight).div(2) + 17)

    override fun onRecipeBookButtonClick() {
        super.onRecipeBookButtonClick()
        val offset = RecipeBookComponent.IMAGE_WIDTH.div(2).plus(4) * (recipeBook?.isVisible ?: false).either(1, -1)
        energy?.x += offset
        efficiency?.x += offset
        progress?.x += offset
    }

    override fun containerTick() {
        energyItemListener.tick(menu)
    }

    override fun hasClickedOutside(mx: Double, my: Double, xo: Int, yo: Int): Boolean {
        val clickedOutside = mx < xo.toDouble() || my < yo.toDouble() || mx >= (xo + imageWidth).toDouble() || my >= (yo + customHeight).toDouble()
        return recipeBook?.hasClickedOutside(mx, my, leftPos, topPos, imageWidth, customHeight) == true && clickedOutside
    }

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick)
        val i = leftPos
        val j = (height - customHeight) / 2
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, i, j, 0.0f, 0.0f, imageWidth, customHeight, 256, 256)

        extractProcessorIcons(graphics)
    }

    protected open fun extractProcessorIcons(graphics: GuiGraphicsExtractor) {
        extractEnergyIcon(graphics)
        extractEfficiencyIcon(graphics)
        extractLastRecipeIcon(graphics)
    }

    protected open fun extractEnergyIcon(graphics: GuiGraphicsExtractor, x: Int? = null, y: Int? = null) {
        val energy = energy?.energy()
        val recipeEnergy = ProcessorMenu.dataSchema.getIntValue(menu.data, ProcessorMenu.ProcessorDataKey.RECIPE_ENERGY) ?: 0
        val recipeTime = ProcessorMenu.dataSchema.getIntValue(menu.data, ProcessorMenu.ProcessorDataKey.RECIPE_TIME) ?: 0
        val speed = menu.clientData?.machineUsage ?: 0f
        val usage = menu.clientData?.machineSpeed ?: 0f
        val total = Mth.ceil(recipeEnergy * usage)
        val cost = Mth.ceil(total * speed / recipeTime)
        if ((energy ?: 0L) > cost) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, energyIndicator, x ?: this.energy?.x?.minus(12) ?: 0, y ?: this.energy?.y?.minus(3) ?: 0, 9, 10)
        }
    }

    protected open fun extractEfficiencyIcon(graphics: GuiGraphicsExtractor, x: Int? = null, y: Int? = null) {
        val completions = ProcessorMenu.dataSchema.getIntValue(menu.data, ProcessorMenu.ProcessorDataKey.COMPLETIONS) ?: 0
        val sameRecipe = ProcessorMenu.dataSchema.getBoolValue(menu.data, ProcessorMenu.ProcessorDataKey.RECIPE_SAME) ?: false
        if (completions > 0 && sameRecipe) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, efficiencyIndicator, x ?: this.efficiency?.x?.minus(14) ?: 0, y ?: this.efficiency?.y?.minus(3) ?: 0, 11, 10)
        }
    }

    protected open fun extractLastRecipeIcon(graphics: GuiGraphicsExtractor, x: Int? = null, y: Int? = null) {
        menu.clientData?.lastRecipeIcon?.ifPresent {
            val item = minecraft.level?.holderLookup(Registries.ITEM)?.get(it)?.getOrNull() ?: return@ifPresent
            val stack = ItemStack(item)
            graphics.fakeItem(stack, x ?: this.efficiency?.x?.minus(32) ?: 0, y ?: this.efficiency?.y?.minus(6) ?: 0)
        }
    }

    override fun extractTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        super.extractTooltip(graphics, mouseX, mouseY)
        if (extractEnergyTooltip(graphics, mouseX, mouseY)) return
        if (extractProgressTooltip(graphics, mouseX, mouseY)) return
        if (extractEfficiencyTooltip(graphics, mouseX, mouseY)) return
    }

    protected open fun extractEnergyTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, areaX: IntRange? = null, areaY: IntRange? = null): Boolean {
        val energy = energy ?: return false
        if (mouseX in (areaX ?: energy.x.minus(12)..energy.right) && mouseY in (areaY ?: (energy.y.minus(3) until energy.y.plus(7)))) {
            val tooltip = TooltipEngine.withLineBreaks(ToBaseTooltipEngine.getEnergyInfo(
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_TOTAL) ?: 0L,
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_CAPACITY) ?: 0L,
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_MAX_INPUT) ?: 0L,
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_MAX_OUTPUT) ?: 0L,
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_INSERT_CHANGE),
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_INSERT_AVERAGE),
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_EXTRACT_CHANGE),
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.ENERGY_EXTRACT_AVERAGE),
                advanced = minecraft.hasShiftDown()
            ))
            graphics.setTooltipForNextFrame(font, font.split(tooltip, 200), mouseX, mouseY)
            return true
        }
        return false
    }

    protected open fun extractProgressTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, areaX: IntRange? = null, areaY: IntRange? = null): Boolean {
        val progress = progress ?: return false
        if (mouseX in (areaX ?: progress.x..progress.right) && mouseY in (areaY ?: progress.y..progress.bottom)) {
            val tooltip = TooltipEngine.withLineBreaks(ToStarsTooltipEngine.getProcessorProgressInfo(
                ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.PROGRESS) ?: 0,
                ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.MAX_PROGRESS) ?: 0,
                ProcessorMenu.dataSchema.getIntValue(menu.data, ProcessorMenu.ProcessorDataKey.RECIPE_TIME) ?: 0,
                ProcessorMenu.dataSchema.getLongValue(menu.data, ProcessorMenu.ProcessorDataKey.RECIPE_ENERGY) ?: 0,
                menu.clientData?.machineUsage ?: 0f,
                menu.clientData?.machineSpeed ?: 0f,
                advanced = minecraft.hasShiftDown()
            ))
            graphics.setTooltipForNextFrame(font, font.split(tooltip, 200), mouseX, mouseY)
            return true
        }
        return false
    }

    protected open fun extractEfficiencyTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, areaX: IntRange? = null, areaY: IntRange? = null): Boolean {
        val efficiency = efficiency ?: return false
        if (mouseX in (areaX ?: efficiency.x.minus(32)..efficiency.right) && mouseY in (areaY ?: (efficiency.y.minus(7) until efficiency.y.plus(9)))) {
            val tooltip = mutableListOf<Component>()
            val recipe = menu.clientData?.lastRecipe?.getOrNull()?.let { Component.translatable("recipe.${it.identifier().toLanguageKey()}") }
            tooltip.addAll(ToStarsTooltipEngine.getEfficiencyInfo(
                recipe,
                recipe != null && !(ProcessorMenu.dataSchema.getBoolValue(menu.data, ProcessorMenu.ProcessorDataKey.RECIPE_SAME) ?: false) && (ProcessorMenu.dataSchema.getIntValue(menu.data, ProcessorMenu.ProcessorDataKey.RECIPE_TIME) ?: 0) > 0,
                ProcessorMenu.dataSchema.getIntValue(menu.data, ProcessorMenu.ProcessorDataKey.COMPLETIONS) ?: 0,
                menu.clientData?.machineBonus ?: 0f,
                menu.clientData?.machineBonusMax ?: 0
            ))
            graphics.setTooltipForNextFrame(font, font.split(TooltipEngine.withLineBreaks(tooltip), 200), mouseX, mouseY)
            return true
        }
        return false
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

        val energyIndicator = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/indicator_full")

        val efficiencyIndicator = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "efficiency")

    }

}
