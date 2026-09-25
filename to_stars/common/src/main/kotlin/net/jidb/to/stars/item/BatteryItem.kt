package net.jidb.to.stars.item

import net.jidb.to.base.pub.item.ToEnergyDisplayItem
import net.jidb.to.stars.info.MachineTier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * A battery, which stores To Energy and shows its charge as a durability bar.
 *
 * @property machine The tier the battery is built at.
 * @param properties The item's own properties.
 */
class BatteryItem(val machine: MachineTier, properties: Properties) : Item(properties) {

    override fun isBarVisible(stack: ItemStack) = true

    override fun getBarColor(stack: ItemStack) = ToEnergyDisplayItem.getEnergyColor(stack)

    override fun getBarWidth(stack: ItemStack) = ToEnergyDisplayItem.getEnergyWidth(stack)

}
