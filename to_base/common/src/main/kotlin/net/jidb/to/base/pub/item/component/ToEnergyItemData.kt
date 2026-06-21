package net.jidb.to.base.pub.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.base.service.Services
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

data class ToEnergyItemData(val energy: Long, val max: Long, val maxInput: Long, val maxOutput: Long = maxInput) : TooltipProvider {

    override fun addToTooltip(context: Item.TooltipContext, consumer: Consumer<Component>, flag: TooltipFlag, components: DataComponentGetter) {
        ToBaseTooltipEngine.getEnergyItemInfo(this, Services.side.hasShiftDown()).forEach(consumer::accept)
        flag.isAdvanced
    }

    companion object {
        val codec = RecordCodecBuilder.create {
            it.group(
                Codec.LONG.fieldOf("energy").forGetter(ToEnergyItemData::energy),
                Codec.LONG.fieldOf("max").forGetter(ToEnergyItemData::max),
                Codec.LONG.fieldOf("maxInput").forGetter(ToEnergyItemData::maxInput),
                Codec.LONG.fieldOf("maxOutput").forGetter(ToEnergyItemData::maxOutput)
            )
                .apply(it, ::ToEnergyItemData)
        }
    }

}