package net.jidb.to.base.pub.item

import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack

/**
 * A helper object that describes the durability-style bar drawn under an item holding To Energy.
 * Vanilla asks an item for a colour and a width, so both are worked out here from how full the item is, in the same way vanilla works out its own damage bar.
 *
 * @since 0.8.0
 */
object ToEnergyDisplayItem {

    /**
     * The colour of the energy bar for an item, which runs from a dim red when empty to a bright yellow when full.
     *
     * @param stack The item stack to read the energy of.
     * @return The packed ARGB colour of the bar, or `0` where the stack holds no energy.
     * @since 0.8.0
     */
    fun getEnergyColor(stack: ItemStack): Int {
        val energy = ToEnergyItemProvider.getTransferContext(stack) ?: return 0
        val fill = energy.getTotalAmount(Unit) / energy.getTotalCapacity(Unit).toFloat()
        return ARGB.color(255, Mth.hsvToRgb(fill.times(0.1f).plus(0.05f), fill.times(0.7f).plus(0.3f), fill.times(0.2f).plus(0.8f)))
    }

    /**
     * The width of the energy bar for an item, in pixels out of the thirteen a full bar occupies.
     *
     * @param stack The item stack to read the energy of.
     * @return The width of the bar, or `0` where the stack holds no energy.
     * @since 0.8.0
     */
    fun getEnergyWidth(stack: ItemStack): Int {
        val energy = ToEnergyItemProvider.getTransferContext(stack) ?: return 0
        val fill = energy.getTotalAmount(Unit) / energy.getTotalCapacity(Unit).toFloat()
        return Mth.ceil(fill.coerceIn(0f, 1f).times(13f))
    }

}
