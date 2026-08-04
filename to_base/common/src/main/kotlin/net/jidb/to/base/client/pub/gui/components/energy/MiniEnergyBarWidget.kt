package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.minecraft.resources.Identifier

/**
 * The small energy bar, for a screen with no room for the full-width [EnergyBarWidget].
 * Unlike the full bar it draws no text, so the stored and total energy are only available through its narration.
 *
 * @param energy A function supplying how much energy is currently stored.
 * @param capacity A function supplying how much energy can be stored.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @since 0.8.0
 */
class MiniEnergyBarWidget(energy: () -> Long, capacity: () -> Long, x: Int, y: Int) : AbstractEnergyBarWidget(energy, capacity, x, y, 33, 4) {

    override fun getBackTexture() = back
    override fun getFrontTexture() = front

    companion object {

        /**
         * The sprite drawn over the bar in proportion to how much energy is stored.
         *
         * @since 0.8.0
         */
        val front = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/minibar_full")

        /**
         * The sprite drawn underneath the bar, showing its full extent.
         *
         * @since 0.8.0
         */
        val back = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/minibar_empty")

    }

}
