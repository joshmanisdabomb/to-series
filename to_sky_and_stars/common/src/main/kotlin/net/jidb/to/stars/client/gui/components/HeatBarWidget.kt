package net.jidb.to.stars.client.gui.components

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.HeatGeneratorMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.MouseButtonInfo
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import net.minecraft.world.inventory.ContainerData

class HeatBarWidget(private val data: ContainerData, x: Int, y: Int, width: Int = 120, height: Int = 16) : AbstractWidget(x, y, width, height, Component.empty()) {

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val raw = HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.HEAT) ?: 0f
        val heat = raw - (HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.INITIAL) ?: 0f)
        val target = heat + (HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.ADD) ?: 0f).times(HeatGeneratorMenu.dataSchema.getShortValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.DURATION) ?: 0)
        val max = HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.RANGE) ?: 0f
        if (max <= 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, empty, x, y, width, height, ARGB.white(alpha))
            return
        }
        val fill = heat / max
        val ghost = target.coerceAtMost(max) / max
        if (fill >= 1f) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Companion.max, x, y, width, height, ARGB.white(alpha))
        } else {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, empty, x, y, width, height, ARGB.white(alpha))
            if (fill > 0f) {
                if (ghost > fill) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Companion.ghost, width, height, 0, 0, x, y, 13 + Mth.floor(ghost * width.minus(13)), height, ARGB.white(alpha))
                }
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, width, height, 0, 0, x, y, 13 + Mth.floor(fill * width.minus(13)), height, ARGB.white(alpha))
            }
        }
    }

    override fun createNarrationMessage() = Component.empty()/*.translatable(
        "tooltip.${ToBaseMod.modid}.energy.stored.value",
        ToBaseTooltipEngine.createSIComponent(energy(), 1, Minecraft.getInstance().hasShiftDown()),
        ToBaseTooltipEngine.createSIComponent(capacity(), 1, Minecraft.getInstance().hasShiftDown())
    )*/

    override fun updateWidgetNarration(output: NarrationElementOutput) {
        output.add(NarratedElementType.TITLE, createNarrationMessage())
    }

    override fun isValidClickButton(buttonInfo: MouseButtonInfo) = false

    companion object {
        val empty = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_empty")
        val ghost = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_ghost")
        val full = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_full")
        val max = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_max")
    }

}
