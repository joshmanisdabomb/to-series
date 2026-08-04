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

/**
 * The data component that stores To Energy on an item stack, along with the limits on how fast it moves.
 * Because the component is both the store and a [ToEnergyItemProvider], any item can be made to hold energy by giving it this component, without its class knowing anything about energy.
 *
 * @property energy The amount of energy currently stored.
 * @property max The amount of energy that can be stored.
 * @property maxInput The amount of energy that can be inserted at once.
 * @property maxOutput The amount of energy that can be extracted at once. Defaults to [maxInput].
 * @since 0.8.0
 */
data class ToEnergyItemComponentData(val energy: Long, val max: Long, val maxInput: Long, val maxOutput: Long = maxInput) : TooltipProvider, ToEnergyItemProvider {

    override fun addToTooltip(context: Item.TooltipContext, consumer: Consumer<Component>, flag: TooltipFlag, components: DataComponentGetter) {
        ToBaseTooltipEngine.getEnergyItemInfo(this, Services.side.hasShiftDown()).forEach(consumer::accept)
    }

    override fun getTransferContext(stack: ItemStack) = ItemComponentToEnergyTransferContext(stack)

    companion object {

        /**
         * The codec this component is saved and loaded with.
         *
         * @since 0.8.0
         */
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
