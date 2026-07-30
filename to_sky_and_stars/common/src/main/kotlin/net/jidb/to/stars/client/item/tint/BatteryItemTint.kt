package net.jidb.to.stars.client.item.tint

import com.mojang.serialization.MapCodec
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class BatteryItemTint : ItemTintSource {

    override fun calculate(stack: ItemStack, level: ClientLevel?, entity: LivingEntity?): Int {
        val energy = ToEnergyItemProvider.getTransferContext(stack)
        val fill = if (energy != null) energy.getTotalAmount(Unit) / energy.getTotalCapacity(Unit).toFloat() else 0f
        return ARGB.color(255, Mth.hsvToRgb(fill.times(0.03f), fill.times(0.4f).plus(0.6f), fill.times(0.5f).plus(0.3f)))
    }

    override fun type() = codec

    companion object {
        val instance = BatteryItemTint()

        val codec = MapCodec.unit(instance)
    }

}
