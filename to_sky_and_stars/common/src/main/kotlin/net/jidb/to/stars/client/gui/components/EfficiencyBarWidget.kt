package net.jidb.to.stars.client.gui.components

import net.jidb.to.base.client.pub.gui.components.AbstractBarWidget
import net.jidb.to.stars.ToStarsMod
import net.minecraft.resources.Identifier

/**
 * The bar showing how efficient a machine has become at the recipe it is running.
 *
 * @property fill How full the bar is, from `0` to `1`.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 */
class EfficiencyBarWidget(val fill: () -> Float, x: Int, y: Int) : AbstractBarWidget(x, y, 33, 4) {

    override fun getFill() = fill()

    override fun getBackTexture() = back
    override fun getFrontTexture() = front

    companion object {

        /**
         * The texture of the filled part of the bar.
         */
        val front = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "efficiency_full")

        /**
         * The texture of the unfilled part of the bar.
         */
        val back = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "efficiency_empty")

    }

}
