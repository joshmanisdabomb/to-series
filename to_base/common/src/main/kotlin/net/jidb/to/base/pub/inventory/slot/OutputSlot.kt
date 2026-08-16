package net.jidb.to.base.pub.inventory.slot

import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

open class OutputSlot(container: Container, slot: Int, x: Int, y: Int) : Slot(container, slot, x, y) {

    override fun mayPlace(stack: ItemStack) = false

    override fun isFake() = true

}
