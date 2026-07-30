package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.world.item.ItemStack

interface ToEnergyItemProvider {

    fun getTransferContext(stack: ItemStack): ToEnergyTransferContext?

    companion object {
        fun getProvider(stack: ItemStack): ToEnergyItemProvider? {
            val provider = stack.item as? ToEnergyItemProvider
            if (provider != null) {
                return provider
            }
            return stack.components.firstNotNullOfOrNull { it.value as? ToEnergyItemProvider }
        }

        fun getTransferContext(stack: ItemStack) = getProvider(stack)?.getTransferContext(stack)
    }

}