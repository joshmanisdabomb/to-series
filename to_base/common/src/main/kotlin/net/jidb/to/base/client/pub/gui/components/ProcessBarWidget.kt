package net.jidb.to.base.client.pub.gui.components

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

class ProcessBarWidget(val fill: () -> Float, x: Int, y: Int, width: Int = 14, height: Int = 14, protected val full: Identifier = Identifier.withDefaultNamespace("container/furnace/lit_progress")) : AbstractWidget(x, y, width, height, Component.empty()) {

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val progress = Mth.ceil(fill() * (height-1)) + 1
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, width, height, 0, height - progress, x, y + (height - progress), width, progress, ARGB.white(alpha))
        extractScrollingStringOverContents(graphics.textRenderer(), createNarrationMessage().withStyle(Style.EMPTY.withShadowColor(0xFF550000.toInt())), 2)
    }

    override fun createNarrationMessage() = Component.empty()

    override fun updateWidgetNarration(output: NarrationElementOutput) {
        output.add(NarratedElementType.TITLE, createNarrationMessage())
    }

    override fun isValidClickButton(buttonInfo: MouseButtonInfo) = false

}
