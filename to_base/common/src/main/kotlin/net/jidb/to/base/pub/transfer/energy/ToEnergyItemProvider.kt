package net.jidb.to.base.pub.transfer.energy

import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.world.item.ItemStack

/**
 * Implemented by anything that can say how much To Energy an item stack holds, i.e. the item itself or one of the data components on the stack.
 * Letting a component answer as well as the item is what allows an existing item to be given energy without its class knowing anything about it.
 *
 * @see ToEnergyWorldlyProvider
 * @since 0.8.0
 */
interface ToEnergyItemProvider {

    /**
     * Retrieves the energy held by the given item stack.
     *
     * @param stack The item stack to read the energy of.
     * @return The [ToEnergyTransferContext] for the stack, or `null` where this provider holds no energy for it.
     * @since 0.8.0
     */
    fun getTransferContext(stack: ItemStack): ToEnergyTransferContext?

    companion object {

        /**
         * Finds the provider for an item stack, preferring the item itself over any of its components.
         *
         * @param stack The item stack to find a provider for.
         * @return The first provider found, or `null` where neither the item nor any of its components is one.
         * @since 0.8.0
         */
        fun getProvider(stack: ItemStack): ToEnergyItemProvider? {
            val provider = stack.item as? ToEnergyItemProvider
            if (provider != null) {
                return provider
            }
            return stack.components.firstNotNullOfOrNull { it.value as? ToEnergyItemProvider }
        }

        /**
         * Retrieves the energy held by an item stack, by finding its provider with [getProvider] and asking that.
         *
         * @param stack The item stack to read the energy of.
         * @return The [ToEnergyTransferContext] for the stack, or `null` where the stack holds no energy.
         * @since 0.8.0
         */
        fun getTransferContext(stack: ItemStack) = getProvider(stack)?.getTransferContext(stack)

    }

}
