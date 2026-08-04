package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier

/**
 * The full-width energy bar, which draws the stored and total energy as text over the bar itself.
 * The text scrolls where it is too long to fit, and carries a dark red shadow so that it stays readable against a full bar.
 *
 * @param energy A function supplying how much energy is currently stored.
 * @param capacity A function supplying how much energy can be stored.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @since 0.6.0
 */
class EnergyBarWidget(energy: () -> Long, capacity: () -> Long, x: Int, y: Int) : AbstractEnergyBarWidget(energy, capacity, x, y, 120, 10) {

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a)
        extractScrollingStringOverContents(graphics.textRenderer(), createNarrationMessage().withStyle(Style.EMPTY.withShadowColor(0xFF550000.toInt())), 2)
    }

    override fun getBackTexture() = back
    override fun getFrontTexture() = front

    companion object {

        /**
         * The sprite drawn over the bar in proportion to how much energy is stored.
         *
         * @since 0.8.0
         */
        val front = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/bar_full")

        /**
         * The sprite drawn underneath the bar, showing its full extent.
         *
         * @since 0.8.0
         */
        val back = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/bar_empty")

    }

}
