package net.jidb.to.base.client.pub.gui.components

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.MouseButtonInfo
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import net.minecraft.util.Mth

class EnergyBarWidget(val energy: () -> Long, val capacity: () -> Long, x: Int, y: Int, width: Int = 120, height: Int = 10, protected val full: Identifier = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/bar_full"), protected val empty: Identifier = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/bar_empty")) : AbstractWidget(x, y, width, height, Component.empty()) {

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, empty, x, y, width, height, ARGB.white(alpha))
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, width, height, 0, 0, x, y, Mth.ceil((energy() / capacity().toFloat()) * width), height, ARGB.white(alpha))
        extractScrollingStringOverContents(graphics.textRenderer(), createNarrationMessage().withStyle(Style.EMPTY.withShadowColor(0xFF550000.toInt())), 2)
    }

    override fun createNarrationMessage() = Component.translatable(
        "tooltip.${ToBaseMod.modid}.energy.stored.value",
        ToBaseTooltipEngine.createSIComponent(energy(), 1, Minecraft.getInstance().hasShiftDown()),
        ToBaseTooltipEngine.createSIComponent(capacity(), 1, Minecraft.getInstance().hasShiftDown())
    )

    override fun updateWidgetNarration(output: NarrationElementOutput) {
        output.add(NarratedElementType.TITLE, createNarrationMessage())
    }

    override fun isValidClickButton(buttonInfo: MouseButtonInfo) = false

}
