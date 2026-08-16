package net.jidb.to.base.pub.inventory.slot

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.service.Services
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class ToEnergyItemSlot(container: Container, index: Int, x: Int, y: Int, private val changeListener: ((container: Container) -> Unit)? = null) : Slot(container, index, x, y) {

    override fun mayPlace(stack: ItemStack): Boolean {
        val itemTransfer = Services.platform.transfer.itemProvider.fromContainer(container, null) ?: return false
        return ToBaseMod.transferProviders.to_energy.fromItemStack(stack, itemTransfer, index) != null
    }

    override fun getMaxStackSize(stack: ItemStack) = 1

    override fun setChanged() {
        super.setChanged()
        changeListener?.invoke(container)
    }

}
