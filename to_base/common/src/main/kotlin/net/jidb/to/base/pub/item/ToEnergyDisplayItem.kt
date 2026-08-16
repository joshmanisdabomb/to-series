package net.jidb.to.base.pub.item

import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack

object ToEnergyDisplayItem {

    fun getEnergyColor(stack: ItemStack): Int {
        val energy = ToEnergyItemProvider.getTransferContext(stack) ?: return 0
        val fill = energy.getTotalAmount(Unit) / energy.getTotalCapacity(Unit).toFloat()
        return ARGB.color(255, Mth.hsvToRgb(fill.times(0.1f).plus(0.05f), fill.times(0.7f).plus(0.3f), fill.times(0.2f).plus(0.8f)))
    }

    fun getEnergyWidth(stack: ItemStack): Int {
        val energy = ToEnergyItemProvider.getTransferContext(stack) ?: return 0
        val fill = energy.getTotalAmount(Unit) / energy.getTotalCapacity(Unit).toFloat()
        return Mth.ceil(fill.coerceIn(0f, 1f).times(13f))
    }

}
