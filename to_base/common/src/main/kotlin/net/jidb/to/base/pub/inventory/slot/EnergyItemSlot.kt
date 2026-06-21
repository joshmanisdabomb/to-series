package net.jidb.to.base.pub.inventory.slot

import net.jidb.to.base.ToBaseMod
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class EnergyItemSlot(container: Container, index: Int, x: Int, y: Int, private val changeListener: ((container: Container) -> Unit)? = null) : Slot(container, index, x, y) {

    override fun mayPlace(stack: ItemStack): Boolean {
        return stack.has(ToBaseMod.itemComponents.energy_data)
    }

    override fun getMaxStackSize(stack: ItemStack) = 1

    override fun setChanged() {
        super.setChanged()
        changeListener?.invoke(container)
    }

}