package net.jidb.to.stars.item

import net.jidb.to.base.pub.item.EnergyDisplayItem
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class BatteryBlockItem(block: Block, properties: Properties) : BlockItem(block, properties), EnergyDisplayItem {

    override fun isBarVisible(stack: ItemStack) = true

    override fun getBarColor(stack: ItemStack) = getEnergyColor(stack)

    override fun getBarWidth(stack: ItemStack) = getEnergyWidth(stack)

}
