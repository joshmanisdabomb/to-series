package net.jidb.to.base.pub.info

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.pub.item.component.ToEnergyItemData
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow

object ToBaseTooltipEngine : TooltipEngine(ToBaseMod.modid) {

    val siUnits = arrayOf(Component.empty()) + arrayOf("k", "m", "g", "t", "p", "e").map { Component.translatable("tooltip.$modid.number.si.$it") }
    val timeUnits = listOf(
        "year" to 20L * 60L * 60L * 24L * 365L,
        "month" to 20L * 60L * 60L * 24L * 30L,
        "week" to 20L * 60L * 60L * 24L * 7L,
        "day" to 20L * 60L * 60L * 24L,
        "hour" to 20L * 60L * 60L,
        "minute" to 20L * 60L,
        "second" to 20L,
        "tick" to 1L
    )

    fun createSIComponent(amount: Long, decimals: Int = 0, ignore: Boolean = false): MutableComponent {
        if (ignore) return Component.translatable("tooltip.$modid.number.si", "", number0dp.format(amount), "")
        if (amount == 0L) return Component.translatable("tooltip.$modid.number.si", "", 0, "")

        val absolute = amount.absoluteValue
        val tier = (log10(absolute.toDouble()) / 3).toInt()

        if (tier == 0) return Component.translatable("tooltip.$modid.number.si", "", amount, "")

        val scaled = absolute / 1000.0.pow(tier.toDouble())
        val suffix = siUnits.getOrNull(tier) ?: Component.empty()

        val formatted = "%.${decimals}f".format(scaled)
        val sign = if (amount < 0) "-" else ""

        return Component.translatable("tooltip.$modid.number.si", sign, formatted, suffix)
    }

    fun createPlusComponent(amount: Long, value: Any): Any {
        if (amount <= 0L) return value
        return Component.translatable("tooltip.$modid.number.plus", value)
    }

    fun createDurationComponent(ticks: Long): MutableComponent {
        val absolute = ticks.absoluteValue
        val sign = if (ticks < 0) "-" else ""

        for ((unit, ticks) in timeUnits) {
            if (absolute >= ticks) {
                val value = absolute.toDouble() / ticks.toDouble()
                val formatted = number1dp.format(value)

                return Component.translatable("tooltip.$modid.duration.$unit" + if (formatted == "1") "" else ".plural", sign, formatted)
            }
        }

        return Component.translatable("tooltip.$modid.duration.tick.plural", "", 0)
    }

    fun getEnergyInfo(energy: Long, capacity: Long, maxInput: Long, maxOutput: Long, insertChange: Long? = null, insertAverage: Long? = null, extractChange: Long? = null, extractAverage: Long? = null, averageDuration: String = "s", advanced: Boolean? = null): List<MutableComponent> {
        val ret = mutableListOf(
            createPropertyComponent("energy", "stored", ChatFormatting.YELLOW.color!!,
                createSIComponent(energy, 1, advanced ?: false),
                createSIComponent(capacity, 1, advanced ?: false)),
        )

        if (maxInput > 0 || maxOutput > 0) {
            if (maxInput == maxOutput && advanced != true) {
                ret.add(createPropertyComponent("energy", "max.io", 0xFFBBAF76.toInt(), createSIComponent(maxInput, 1, advanced ?: false)))
            } else {
                if (maxInput > 0) {
                    ret.add(createPropertyComponent("energy", "max.input", 0xFFDAFF76.toInt(), createSIComponent(maxInput, 1, advanced ?: false)))
                }
                if (maxOutput > 0) {
                    ret.add(createPropertyComponent("energy", "max.output", 0xFFFFB27F.toInt(), createSIComponent(maxOutput, 1, advanced ?: false)))
                }
            }
        }

        if (advanced == true) {
            if (insertChange != null) {
                ret.add(createPropertyComponent("energy", "insert", 0xFFDAFF7F.toInt(), createPlusComponent(insertChange, createSIComponent(insertChange, 1, true))))
            }
            if (extractChange != null) {
                ret.add(createPropertyComponent("energy", "extract", 0xFFFFB27F.toInt(), createSIComponent(-extractChange, 1, true)))
            }
            if (insertAverage != null || extractAverage != null) {
                val average = (insertAverage ?: 0L) - (extractAverage ?: 0L)
                ret.add(createPropertyComponent("energy", "change.avg", when {
                    average > 0L -> 0xFFDAFF7F.toInt()
                    average < 0L -> 0xFFFFB27F.toInt()
                    else -> 0xFFBBAF76.toInt()
                }, createPlusComponent(average, createSIComponent(average, 1, true)), averageDuration))
            }
        } else {
            if (insertChange != null || extractChange != null) {
                val change = (insertChange ?: 0L) - (extractChange ?: 0L)
                ret.add(createPropertyComponent("energy", "change", when {
                    change > 0L -> 0xFFDAFF7F.toInt()
                    change < 0L -> 0xFFFFB27F.toInt()
                    else -> 0xFFBBAF76.toInt()
                }, createPlusComponent(change, createSIComponent(change, 1, false))))
            }
        }

        if (insertAverage != null || extractAverage != null) {
            val change = (insertChange ?: 0L) - (extractChange ?: 0L)
            if (change > 0L) {
                val ticks = (capacity - energy) / change
                ret.add(createPropertyComponent("energy", "result.full", ChatFormatting.WHITE.color!!, createDurationComponent(ticks)))
            } else if (change < 0L) {
                val ticks = energy / -change
                ret.add(createPropertyComponent("energy", "result.empty", ChatFormatting.WHITE.color!!, createDurationComponent(ticks)))
            }
        }

        if (advanced == false) {
            ret.add(Component.translatable("tooltip.$modid.more", Component.keybind("key.$modid.keyboard.shift").withStyle(ChatFormatting.BOLD))
                .withStyle(Style.EMPTY.withColor(0xFF562CCB.toInt())))
        }

        return ret
    }

    fun getEnergyItemInfo(data: ToEnergyItemData, advanced: Boolean? = null) = getEnergyInfo(data.energy, data.max, data.maxInput, data.maxOutput, advanced = advanced)

}