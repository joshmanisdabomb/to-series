package net.jidb.to.stars.item

import net.jidb.to.base.pub.item.EnergyDisplayItem
import net.jidb.to.stars.info.MachineTier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class BatteryItem(val machine: MachineTier, properties: Properties) : Item(properties), EnergyDisplayItem {

    override fun isBarVisible(stack: ItemStack) = true

    override fun getBarColor(stack: ItemStack) = getEnergyColor(stack)

    override fun getBarWidth(stack: ItemStack) = getEnergyWidth(stack)

}
