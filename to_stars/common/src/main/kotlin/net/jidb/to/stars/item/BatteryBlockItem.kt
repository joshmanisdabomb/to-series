package net.jidb.to.stars.item

import net.jidb.to.base.pub.item.ToEnergyDisplayItem
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

/**
 * The item of a block that stores To Energy, which shows its charge as a durability bar.
 *
 * @param block The block this is the item of.
 * @param properties The item's own properties.
 */
class BatteryBlockItem(block: Block, properties: Properties) : BlockItem(block, properties) {

    override fun isBarVisible(stack: ItemStack) = true

    override fun getBarColor(stack: ItemStack) = ToEnergyDisplayItem.getEnergyColor(stack)

    override fun getBarWidth(stack: ItemStack) = ToEnergyDisplayItem.getEnergyWidth(stack)

}
