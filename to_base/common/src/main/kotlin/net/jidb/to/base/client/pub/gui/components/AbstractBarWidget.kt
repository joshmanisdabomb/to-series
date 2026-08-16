package net.jidb.to.base.client.pub.gui.components

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

abstract class AbstractBarWidget(x: Int, y: Int, width: Int, height: Int) : AbstractWidget(x, y, width, height, Component.empty()) {

    open val animation: AnimationDirection = AnimationDirection.LEFT_RIGHT

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val back = getBackTexture()
        if (back != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, back, x, y, width, height, ARGB.white(alpha))
        }
        val front = getFrontTexture()
        if (front != null) {
            val fill = getFill().coerceIn(0f, 1f)
            when (animation) {
                AnimationDirection.BOTTOM_TOP -> {
                    val th = Mth.ceil(fill * height)
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, front, width, height, 0, height - th, x, y + (height - th), width, th, ARGB.white(alpha))
                }
                AnimationDirection.RIGHT_LEFT -> {
                    val tw = Mth.ceil(fill * width)
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, front, width, height, width - tw, 0, x + (width - tw), y, tw, height, ARGB.white(alpha))
                }
                AnimationDirection.TOP_BOTTOM -> {
                    val th = Mth.ceil(fill * height)
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, front, width, height, 0, 0, x, y, width, th, ARGB.white(alpha))
                }
                else -> {
                    val tw = Mth.ceil(fill * width)
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, front, width, height, 0, 0, x, y, tw, height, ARGB.white(alpha))
                }
            }
        }
    }

    abstract fun getFill(): Float

    abstract fun getBackTexture(): Identifier?
    abstract fun getFrontTexture(): Identifier?

    override fun createNarrationMessage() = Component.empty()

    override fun updateWidgetNarration(output: NarrationElementOutput) {
        output.add(NarratedElementType.TITLE, createNarrationMessage())
    }

    override fun isValidClickButton(buttonInfo: MouseButtonInfo) = false

    enum class AnimationDirection {

        LEFT_RIGHT,
        RIGHT_LEFT,
        BOTTOM_TOP,
        TOP_BOTTOM

    }

}
