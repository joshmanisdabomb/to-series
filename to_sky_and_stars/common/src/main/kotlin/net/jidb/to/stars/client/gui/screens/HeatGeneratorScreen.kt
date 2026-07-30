package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.pub.gui.components.BurnBarWidget
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.gui.components.HeatBarWidget
import net.jidb.to.stars.info.ToStarsTooltipEngine
import net.jidb.to.stars.inventory.menu.HeatGeneratorMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack

class HeatGeneratorScreen<M : HeatGeneratorMenu>(menu: M, playerInventory: Inventory, title: Component) : AbstractContainerScreen<M>(menu, playerInventory, title, 176, 154) {

    private var thermometer: HeatBarWidget? = null
    private var progress: BurnBarWidget? = null

    init {
        inventoryLabelY = imageHeight - 94
    }

    override fun init() {
        super.init()

        thermometer = this.addRenderableWidget(
            HeatBarWidget(menu.data, leftPos + 28, topPos + 19)
        )
        progress = this.addRenderableWidget(
            BurnBarWidget({
                val current = HeatGeneratorMenu.dataSchema.getShortValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.DURATION) ?: 0
                val max = HeatGeneratorMenu.dataSchema.getShortValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.MAX_DURATION) ?: 0
                current.div(max.toFloat())
            }, leftPos + 94, topPos + 42)
        )
    }

    override fun containerTick() {

    }

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick)
        val i = leftPos
        val j = (height - imageHeight) / 2
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, i, j, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256)
    }

    override fun extractTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        super.extractTooltip(graphics, mouseX, mouseY)
        if (thermometer?.isHovered == true) {
            val duration = HeatGeneratorMenu.dataSchema.getShortValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.DURATION) ?: 0
            val initial = HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.INITIAL) ?: 0f
            val range = HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.RANGE) ?: 0f
            val heat = HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.HEAT) ?: 0f
            val target = if (duration <= 0) 0f else (HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.ADD) ?: 0f).times(duration).plus(heat).coerceAtMost(initial + range)
            val tooltip = TooltipEngine.withLineBreaks(ToStarsTooltipEngine.getGeneratorHeatInfo(
                heat,
                target,
                HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.CHANGE) ?: 0f,
                HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.VALUE) ?: 0f,
                HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.SPEED) ?: 0f,
                initial,
                range,
                HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.BONUS) ?: 0f,
                HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.COOLING) ?: 0f,
                advanced = minecraft.hasShiftDown()
            ))
            graphics.setTooltipForNextFrame(font, font.split(tooltip, 200), mouseX, mouseY)
        } else if (progress?.isHovered == true) {
            val tooltip = TooltipEngine.withLineBreaks(ToStarsTooltipEngine.getGeneratorBurnInfo(
                HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.ADD) ?: 0f,
                HeatGeneratorMenu.dataSchema.getShortValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.DURATION) ?: 0
            ))
            graphics.setTooltipForNextFrame(font, font.split(tooltip, 200), mouseX, mouseY)
        }
    }

    override fun getTooltipFromContainerItem(stack: ItemStack): List<Component> {
        val level = minecraft.level ?: return super.getTooltipFromContainerItem(stack)
        val player = minecraft.player ?: return super.getTooltipFromContainerItem(stack)
        val value = menu.getFuelValue(stack, level)
        val duration = menu.getFuelDuration(stack, level)
        if (duration > 0 || value > 0) {
            val remaining = HeatGeneratorMenu.dataSchema.getShortValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.DURATION) ?: 0
            val heat = HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.HEAT) ?: 0f
            val initial = HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.INITIAL) ?: 0f
            val range = HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.RANGE) ?: 0f
            val target = (if (remaining <= 0) heat else (HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.ADD) ?: 0f).times(remaining).plus(heat)).minus(initial).coerceIn(0f, range)
            val info = ToStarsTooltipEngine.getGeneratorFuelInfo(
                stack,
                value,
                duration,
                target,
                range,
                HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.VALUE) ?: 0f,
                HeatGeneratorMenu.dataSchema.getFloatValue(menu.data, HeatGeneratorMenu.HeatGeneratorDataKey.SPEED) ?: 0f,
                minecraft.hasShiftDown()
            )
            return TooltipEngine.injectItemTooltip(stack, info, level, player, minecraft.options.advancedItemTooltips)
        }
        return super.getTooltipFromContainerItem(stack)
    }

    companion object {
        val texture = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/gui/heat_generator.png")
    }

}