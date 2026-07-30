package net.jidb.to.base.pub.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.base.pub.transfer.energy.ItemComponentToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.jidb.to.base.service.Services
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

data class ToEnergyItemComponentData(val energy: Long, val max: Long, val maxInput: Long, val maxOutput: Long = maxInput) : TooltipProvider, ToEnergyItemProvider {

    override fun addToTooltip(context: Item.TooltipContext, consumer: Consumer<Component>, flag: TooltipFlag, components: DataComponentGetter) {
        ToBaseTooltipEngine.getEnergyItemInfo(this, Services.side.hasShiftDown()).forEach(consumer::accept)
    }

    override fun getTransferContext(stack: ItemStack) = ItemComponentToEnergyTransferContext(stack)

    companion object {
        val codec = RecordCodecBuilder.create {
            it.group(
                Codec.LONG.fieldOf("energy").forGetter(ToEnergyItemComponentData::energy),
                Codec.LONG.fieldOf("max").forGetter(ToEnergyItemComponentData::max),
                Codec.LONG.fieldOf("maxInput").forGetter(ToEnergyItemComponentData::maxInput),
                Codec.LONG.fieldOf("maxOutput").forGetter(ToEnergyItemComponentData::maxOutput)
            )
                .apply(it, ::ToEnergyItemComponentData)
        }
    }

}