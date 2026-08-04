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

/**
 * A widget that draws a progress bar, as a foreground texture revealed over a background one in proportion to how full it is.
 * Subclasses supply the two textures and the fill, so that a bar for fuel, progress or energy differs only in what it reads and what it looks like.
 *
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @param width The width of the widget, which is also the width of both textures.
 * @param height The height of the widget, which is also the height of both textures.
 * @since 0.8.0
 */
abstract class AbstractBarWidget(x: Int, y: Int, width: Int, height: Int) : AbstractWidget(x, y, width, height, Component.empty()) {

    /**
     * The direction the bar fills in. Defaults to [AnimationDirection.LEFT_RIGHT].
     *
     * @since 0.8.0
     */
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

    /**
     * How full the bar currently is, which subclasses read from whatever the bar is showing.
     *
     * @return A fraction between `0` and `1`, which is clamped into that range before it is drawn.
     * @since 0.8.0
     */
    abstract fun getFill(): Float

    /**
     * The texture drawn underneath the bar, at its full size.
     *
     * @return The background texture, or `null` where the bar draws straight onto the screen behind it.
     * @since 0.8.0
     */
    abstract fun getBackTexture(): Identifier?

    /**
     * The texture drawn over the background, revealed in proportion to [getFill].
     *
     * @return The foreground texture, or `null` where the bar has nothing to reveal.
     * @since 0.8.0
     */
    abstract fun getFrontTexture(): Identifier?

    override fun createNarrationMessage() = Component.empty()

    override fun updateWidgetNarration(output: NarrationElementOutput) {
        output.add(NarratedElementType.TITLE, createNarrationMessage())
    }

    override fun isValidClickButton(buttonInfo: MouseButtonInfo) = false

    /**
     * Enum that defines the directions a bar can fill in.
     *
     * @see AbstractBarWidget.animation
     * @since 0.8.0
     */
    enum class AnimationDirection {

        /**
         * The bar fills from its left edge towards its right, as a furnace's progress arrow does.
         *
         * @since 0.8.0
         */
        LEFT_RIGHT,

        /**
         * The bar fills from its right edge towards its left.
         *
         * @since 0.8.0
         */
        RIGHT_LEFT,

        /**
         * The bar fills from its bottom edge towards its top, as a furnace's fuel flame does.
         *
         * @since 0.8.0
         */
        BOTTOM_TOP,

        /**
         * The bar fills from its top edge towards its bottom.
         *
         * @since 0.8.0
         */
        TOP_BOTTOM

    }

}
