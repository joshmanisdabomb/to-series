package net.jidb.to.base.pub.info

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.KotlinHelper.orNull
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.pub.item.component.ToEnergyItemComponentData
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import java.text.DecimalFormat
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow

object ToBaseTooltipEngine : TooltipEngine(ToBaseMod.modid) {

    val siPrefixes = arrayOf("n", "u", "-m", "", "k", "m", "g", "t", "p", "e")
    val siComponents = siPrefixes.map { if (it != "") Component.translatable("tooltip.$modid.number.si.$it") else Component.empty() }
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

    val none = Component.translatable("tooltip.$modid.none")

    fun createSIComponent(amount: Long, components: List<Component>, format: DecimalFormat = number0dp, forceTier: Int? = null): MutableComponent {
        if (amount == 0L) return Component.translatable("tooltip.$modid.number.si", "", format.format(0f), "")

        val absolute = amount.absoluteValue
        val tier = forceTier ?: (log10(absolute.toDouble()) / 3).toInt()
        val scaled = absolute / 1000.0.pow(tier.toDouble())
        val suffix = components.getOrNull(tier) ?: Component.empty()

        val formatted = format.format(scaled)
        val sign = if (amount < 0) "-" else ""

        return Component.translatable("tooltip.$modid.number.si", sign, formatted, suffix)
    }

    fun createSIComponent(amount: Long, format: DecimalFormat = number0dp, initialUnit: String = "", maxUnit: String? = null, forceUnit: String? = null): MutableComponent {
        var units = siComponents

        val take = siPrefixes.indexOf(maxUnit).takeIf { it >= 0 }?.plus(1)
        if (take != null) {
            units = units.take(take)
        }
        val drop = siPrefixes.indexOf(initialUnit).takeIf { it >= 0 } ?: 3
        units = units.drop(drop)

        val forceTier = siPrefixes.indexOf(forceUnit).takeIf { it >= 0 }?.minus(drop)
        return createSIComponent(amount, units, format, forceTier)
    }

    fun createPlusComponent(amount: Number, value: Any): Any {
        if (amount.toFloat() <= 0f) return value
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
            createPropertyComponent("energy", "stored", TextColor.YELLOW.value,
                createSIComponent(energy, number1rdp, "-m", forceUnit = (advanced ?: false).orNull("")),
                createSIComponent(capacity, number1rdp, "-m", forceUnit = (advanced ?: false).orNull(""))),
        )

        if (maxInput > 0 || maxOutput > 0) {
            if (maxInput == maxOutput && advanced != true) {
                ret.add(createPropertyComponent("energy", "max.io", 0xFFBBAF76.toInt(), createSIComponent(maxInput, number1rdp, "-m", forceUnit = (advanced ?: false).orNull(""))))
            } else {
                if (maxInput > 0) {
                    ret.add(createPropertyComponent("energy", "max.input", 0xFFDAFF76.toInt(), createSIComponent(maxInput, number1rdp, "-m", forceUnit = (advanced ?: false).orNull(""))))
                }
                if (maxOutput > 0) {
                    ret.add(createPropertyComponent("energy", "max.output", 0xFFFFB27F.toInt(), createSIComponent(maxOutput, number1rdp, "-m", forceUnit = (advanced ?: false).orNull(""))))
                }
            }
        }

        if (advanced == true) {
            if (insertChange != null) {
                ret.add(createPropertyComponent("energy", "insert", 0xFFDAFF7F.toInt(), createPlusComponent(insertChange, createSIComponent(insertChange, number1rdp, "-m", forceUnit = ""))))
            }
            if (extractChange != null) {
                ret.add(createPropertyComponent("energy", "extract", 0xFFFFB27F.toInt(), createSIComponent(-extractChange, number1rdp, "-m", forceUnit = "")))
            }
            if (insertAverage != null || extractAverage != null) {
                val average = (insertAverage ?: 0L) - (extractAverage ?: 0L)
                ret.add(createPropertyComponent("energy", "change.avg", when {
                    average > 0L -> 0xFFDAFF7F.toInt()
                    average < 0L -> 0xFFFFB27F.toInt()
                    else -> 0xFFBBAF76.toInt()
                }, createPlusComponent(average, createSIComponent(average, number1rdp, "-m", forceUnit = "")), averageDuration))
            }
        } else {
            if (insertChange != null || extractChange != null) {
                val change = (insertChange ?: 0L) - (extractChange ?: 0L)
                ret.add(createPropertyComponent("energy", "change", when {
                    change > 0L -> 0xFFDAFF7F.toInt()
                    change < 0L -> 0xFFFFB27F.toInt()
                    else -> 0xFFBBAF76.toInt()
                }, createPlusComponent(change, createSIComponent(change, number1rdp, "-m"))))
            }
        }

        if (insertAverage != null || extractAverage != null) {
            val change = (insertChange ?: 0L) - (extractChange ?: 0L)
            if (change > 0L) {
                val ticks = (capacity - energy) / change
                ret.add(createPropertyComponent("energy", "result.full", TextColor.WHITE.value, createDurationComponent(ticks)))
            } else if (change < 0L) {
                val ticks = energy / -change
                ret.add(createPropertyComponent("energy", "result.empty", TextColor.WHITE.value, createDurationComponent(ticks)))
            }
        }

        if (advanced == false) {
            ret.add(getAdvancedPrompt())
        }

        return ret
    }

    fun getEnergyItemInfo(data: ToEnergyItemComponentData, advanced: Boolean? = null) = getEnergyInfo(data.energy, data.max, data.maxInput, data.maxOutput, advanced = advanced)

    fun getAdvancedPrompt() = Component.translatable("tooltip.$modid.more", Component.keybind("key.$modid.keyboard.shift").withStyle(Style.EMPTY.withBold(true)))
        .withStyle(Style.EMPTY.withColor(0xFF562CCB.toInt()))

}