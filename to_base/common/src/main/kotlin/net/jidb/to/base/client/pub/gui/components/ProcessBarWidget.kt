package net.jidb.to.base.client.pub.gui.components

import net.minecraft.resources.Identifier

/**
 * An [AbstractBarWidget] that draws the arrow showing how far a machine is through what it is making, using vanilla's own furnace sprite.
 * There is no background texture, so the arrow fills over whatever the screen already drew behind it.
 *
 * @property fill A function supplying how far through the process the machine is, as a fraction between `0` and `1`.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @since 0.7.0
 */
open class ProcessBarWidget(val fill: () -> Float, x: Int, y: Int) : AbstractBarWidget(x, y, 24, 16) {

    override fun getFill() = fill()

    override fun getBackTexture() = null
    override fun getFrontTexture() = front

    companion object {

        /**
         * The vanilla furnace progress arrow sprite the bar is drawn from.
         *
         * @since 0.8.0
         */
        val front: Identifier = Identifier.withDefaultNamespace("container/furnace/burn_progress")

    }

}
