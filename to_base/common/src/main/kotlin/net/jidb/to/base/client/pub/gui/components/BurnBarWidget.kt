package net.jidb.to.base.client.pub.gui.components

import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

/**
 * An [AbstractBarWidget] that draws the flame showing how much burn time a fuel has left, using vanilla's own furnace sprite.
 * There is no background texture, so the flame burns down over whatever the screen already drew behind it.
 *
 * @property fill A function supplying how much burn time is left, as a fraction between `0` and `1`.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @since 0.8.0
 */
class BurnBarWidget(val fill: () -> Float, x: Int, y: Int) : AbstractBarWidget(x, y, 14, 14) {

    override val animation: AnimationDirection = AnimationDirection.BOTTOM_TOP

    override fun getFill() = fill()

    override fun getBackTexture() = null
    override fun getFrontTexture() = front

    override fun createNarrationMessage() = Component.empty()

    override fun updateWidgetNarration(output: NarrationElementOutput) {
        output.add(NarratedElementType.TITLE, createNarrationMessage())
    }

    companion object {

        /**
         * The vanilla furnace flame sprite the bar is drawn from.
         *
         * @since 0.8.0
         */
        val front: Identifier = Identifier.withDefaultNamespace("container/furnace/lit_progress")

    }

}
