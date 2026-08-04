package net.jidb.to.stars.item.component

import com.mojang.serialization.MapCodec
import net.jidb.to.base.pub.transfer.energy.InfiniteToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.minecraft.world.item.ItemStack

/**
 * The component marking an item as holding endless To Energy, which is what the creative power source's item carries.
 *
 * It holds nothing of its own, so every instance of it is the same one.
 */
object InfiniteEnergyItemComponent : ToEnergyItemProvider {

    /**
     * The codec the component is saved and loaded through, which carries nothing.
     */
    val codec = MapCodec.unit(InfiniteEnergyItemComponent).codec()

    override fun getTransferContext(stack: ItemStack) = InfiniteToEnergyTransferContext

    override fun equals(other: Any?) = other === InfiniteEnergyItemComponent

    override fun hashCode() = super.hashCode()

}
