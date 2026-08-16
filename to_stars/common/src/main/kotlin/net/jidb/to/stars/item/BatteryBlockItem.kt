package net.jidb.to.stars.item

import net.jidb.to.base.pub.item.ToEnergyDisplayItem
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class BatteryBlockItem(block: Block, properties: Properties) : BlockItem(block, properties) {

    override fun isBarVisible(stack: ItemStack) = true

    override fun getBarColor(stack: ItemStack) = ToEnergyDisplayItem.getEnergyColor(stack)

    override fun getBarWidth(stack: ItemStack) = ToEnergyDisplayItem.getEnergyWidth(stack)

}
