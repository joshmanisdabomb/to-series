package net.jidb.to.base.pub.inventory.slot

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.service.Services
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

/**
 * A slot that only accepts an item holding To Energy, i.e. the charging slot of a machine.
 * Only one item is allowed at a time, because energy is stored per stack rather than per item, so a stack of two would charge as if it were one.
 *
 * @param container The container the slot reads from.
 * @param index The index of the slot within the container.
 * @param x The x position of the slot in the screen.
 * @param y The y position of the slot in the screen.
 * @param changeListener A function called with the container whenever the slot's contents change, or `null` for no callback. Defaults to `null`.
 * @since 0.8.0
 */
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
