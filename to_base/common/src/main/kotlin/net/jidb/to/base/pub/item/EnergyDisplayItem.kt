package net.jidb.to.base.pub.item

import net.jidb.to.base.ToBaseMod
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack

interface EnergyDisplayItem {

    fun getEnergyColor(stack: ItemStack): Int {
        val energy = stack.get(ToBaseMod.itemComponents.energy_data) ?: return 0
        val fill = energy.energy / energy.max.toFloat()
        return ARGB.color(255, Mth.hsvToRgb(fill.times(0.1f).plus(0.05f), fill.times(0.7f).plus(0.3f), fill.times(0.2f).plus(0.8f)))
    }

    fun getEnergyWidth(stack: ItemStack): Int {
        val energy = stack.get(ToBaseMod.itemComponents.energy_data) ?: return 0
        val fill = energy.energy / energy.max.toFloat()
        return Mth.ceil(fill.coerceIn(0f, 1f).times(13f))
    }

}