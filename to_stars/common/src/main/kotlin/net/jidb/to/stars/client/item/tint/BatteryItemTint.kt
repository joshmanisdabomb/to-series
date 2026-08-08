package net.jidb.to.stars.client.item.tint

import com.mojang.serialization.MapCodec
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

/**
 * Colours a battery's overlay by how much charge it is holding, running from a dull red when empty to a bright yellow when full.
 */
class BatteryItemTint : ItemTintSource {

    override fun calculate(stack: ItemStack, level: ClientLevel?, entity: LivingEntity?): Int {
        val energy = ToEnergyItemProvider.getTransferContext(stack)
        val fill = if (energy != null) energy.getTotalAmount(Unit) / energy.getTotalCapacity(Unit).toFloat() else 0f
        return ARGB.color(255, Mth.hsvToRgb(fill.times(0.03f), fill.times(0.4f).plus(0.6f), fill.times(0.5f).plus(0.3f)))
    }

    override fun type() = codec

    companion object {

        /**
         * The one tint source of this kind there is, since it holds nothing to tell one from another.
         */
        val instance = BatteryItemTint()

        /**
         * The codec the tint source is read through, which carries nothing.
         */
        val codec = MapCodec.unit(instance)

    }

}
