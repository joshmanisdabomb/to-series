package net.jidb.to.base.api.info

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.world.item.component.ItemLore
import java.text.DecimalFormat

abstract class TooltipEngine(val modid: String) {

    fun createPropertyComponent(topic: String, type: String, color: Int, vararg value: Any, prefix: String = "tooltip"): MutableComponent {
        val suffix = if (type.isNotEmpty()) ".$type" else ""
        return Component.translatable("$prefix.${modid}.$topic$suffix",
            Component.translatable("$prefix.${modid}.$topic$suffix.value", *value)
                .withStyle(Style.EMPTY.withColor(color)))
            .withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
    }

    companion object {
        val number0dp = DecimalFormat("###,###")
        val number1dp = DecimalFormat("###,###.#")
        val number2dp = DecimalFormat("###,###.##")

        fun withLineBreaks(components: List<Component>): MutableComponent {
            val ret = Component.empty()
            components.dropLast(1).forEach { ret.append(it).append("\n") }
            ret.append(components.last())
            return ret
        }

        fun asItemLore(tooltip: List<Component>) = ItemLore(tooltip, tooltip)
    }

}