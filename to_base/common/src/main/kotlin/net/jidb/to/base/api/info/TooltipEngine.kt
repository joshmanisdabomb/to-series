package net.jidb.to.base.api.info

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.level.Level
import java.text.DecimalFormat

abstract class TooltipEngine(val modid: String) {

    fun createPropertyComponent(topic: String, type: String, color: Int, vararg value: Any, prefix: String = "tooltip"): MutableComponent {
        val suffix = if (type.isNotEmpty()) ".$type" else ""
        return Component.translatable("$prefix.${modid}.$topic$suffix",
            Component.translatable("$prefix.${modid}.$topic$suffix.value", *value)
                .withStyle(Style.EMPTY.withColor(color)))
            .withStyle(Style.EMPTY.withColor(TextColor.GRAY))
    }

    companion object {
        val number0dp = DecimalFormat("###,##0")
        val number1dp = DecimalFormat("###,##0.#")
        val number1rdp = DecimalFormat("###,##0.0")
        val number2dp = DecimalFormat("###,##0.##")
        val number2rdp = DecimalFormat("###,##0.00")
        val number3dp = DecimalFormat("###,##0.###")
        val number3rdp = DecimalFormat("###,##0.000")

        fun withLineBreaks(components: List<Component>): MutableComponent {
            val ret = Component.empty()
            components.dropLast(1).forEach { ret.append(it).append("\n") }
            ret.append(components.last())
            return ret
        }

        fun asItemLore(tooltip: List<Component>) = ItemLore(tooltip, tooltip)

        fun injectItemTooltip(stack: ItemStack, tooltips: List<Component>, level: Level, player: Player, advanced: Boolean): List<Component> {
            val advanced = stack.getTooltipLines(Item.TooltipContext.of(level), player, if (advanced) TooltipFlag.Default.ADVANCED else TooltipFlag.Default.NORMAL)
            val normal = stack.getTooltipLines(Item.TooltipContext.of(level), player, TooltipFlag.Default.NORMAL)

            return normal + tooltips + (advanced - normal)
        }
    }

}