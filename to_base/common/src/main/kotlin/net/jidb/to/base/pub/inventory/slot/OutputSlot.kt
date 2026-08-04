package net.jidb.to.base.pub.inventory.slot

import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

/**
 * A slot that only ever gives items out, so nothing can be put into it by hand.
 * It reports itself as fake so that the client does not try to predict what a click on it does, which is what vanilla's own result slots do.
 *
 * @param container The container the slot reads from.
 * @param slot The index of the slot within the container.
 * @param x The x position of the slot in the screen.
 * @param y The y position of the slot in the screen.
 * @since 0.8.0
 */
open class OutputSlot(container: Container, slot: Int, x: Int, y: Int) : Slot(container, slot, x, y) {

    override fun mayPlace(stack: ItemStack) = false

    override fun isFake() = true

}
