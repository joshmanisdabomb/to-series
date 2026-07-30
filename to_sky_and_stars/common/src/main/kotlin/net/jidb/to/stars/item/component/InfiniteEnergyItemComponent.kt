package net.jidb.to.stars.item.component

import com.mojang.serialization.MapCodec
import net.jidb.to.base.pub.transfer.energy.InfiniteToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.minecraft.world.item.ItemStack

object InfiniteEnergyItemComponent : ToEnergyItemProvider {

    val codec = MapCodec.unit(InfiniteEnergyItemComponent).codec()

    override fun getTransferContext(stack: ItemStack) = InfiniteToEnergyTransferContext

    override fun equals(other: Any?) = other === InfiniteEnergyItemComponent

    override fun hashCode() = super.hashCode()

}
