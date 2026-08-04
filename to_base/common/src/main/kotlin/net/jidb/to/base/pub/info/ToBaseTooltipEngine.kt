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

/**
 * [TooltipEngine] implementation for the base mod, which is where the shapes every To mod's tooltips are written in live: a figure with an SI prefix on it, a duration read out in the largest unit that fits, and the block of lines describing what something holds of To Energy.
 *
 * Most of it takes an `advanced` flag, which is `null` where a tooltip is not being expanded at all, `false` while it is collapsed and `true` once the player holds shift.
 * A collapsed tooltip rounds a figure to whatever prefix suits it, while an expanded one writes it out in full.
 *
 * @since 0.6.0
 */
object ToBaseTooltipEngine : TooltipEngine(ToBaseMod.modid) {

    /**
     * The SI prefixes a figure can be written with, smallest first, where `-m` is the unprefixed unit that a thousandth of the displayed one is counted in.
     *
     * @since 0.8.0
     */
    val siPrefixes = arrayOf("n", "u", "-m", "", "k", "m", "g", "t", "p", "e")

    /**
     * The translated form of each of [siPrefixes], so that a prefix reads correctly in a language that does not use the Latin one.
     *
     * @since 0.8.0
     */
    val siComponents = siPrefixes.map { if (it != "") Component.translatable("tooltip.$modid.number.si.$it") else Component.empty() }

    /**
     * The units a duration can be read out in, longest first, each with how many ticks it lasts.
     *
     * @since 0.6.0
     */
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

    /**
     * The line shown where a tooltip would otherwise say nothing at all.
     *
     * @since 0.8.0
     */
    val none = Component.translatable("tooltip.$modid.none")

    /**
     * Writes a figure out with the SI prefix that suits its size, i.e. `1.2k` rather than `1200`.
     *
     * @param amount The figure to write out.
     * @param components The prefixes to choose between, smallest first.
     * @param format How the figure itself is formatted. Defaults to no decimal places.
     * @param forceTier The prefix to use whatever the size of the figure, as an index into [components], or `null` to pick the one that suits. Defaults to `null`.
     * @return The written-out figure.
     * @since 0.6.0
     */
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

    /**
     * Writes a figure out with the SI prefix that suits its size, naming the prefixes by their own names rather than by where they fall in the list.
     *
     * @param amount The figure to write out, counted in [initialUnit].
     * @param format How the figure itself is formatted. Defaults to no decimal places.
     * @param initialUnit The prefix the figure is already counted in. Defaults to the unprefixed unit.
     * @param maxUnit The largest prefix that may be used, beyond which the figure simply grows, or `null` for no limit. Defaults to `null`.
     * @param forceUnit The prefix to use whatever the size of the figure, or `null` to pick the one that suits. Defaults to `null`.
     * @return The written-out figure.
     * @since 0.6.0
     */
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

    /**
     * Puts a `+` in front of a figure that has already been written out, where it is a gain rather than a loss.
     * A negative figure carries its own sign, so nothing is added to it.
     *
     * @param amount The figure deciding whether a sign is wanted.
     * @param value The written-out figure.
     * @return The figure, signed where it is positive.
     * @since 0.6.0
     */
    fun createPlusComponent(amount: Number, value: Any): Any {
        if (amount.toFloat() <= 0f) return value
        return Component.translatable("tooltip.$modid.number.plus", value)
    }

    /**
     * Writes a duration out in the largest of [timeUnits] that it fills at least once, i.e. `1.5 hours` rather than a count of ticks.
     *
     * @param ticks How long the duration lasts, in ticks.
     * @return The written-out duration.
     * @since 0.6.0
     */
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

    /**
     * The whole block of lines describing what something holds of To Energy: what is stored of what it can hold, what it moves per tick, what it has been moving lately and how long it has left at that rate.
     *
     * @param energy How much energy is stored.
     * @param capacity How much energy can be stored.
     * @param maxInput How much energy can be inserted per tick.
     * @param maxOutput How much energy can be extracted per tick.
     * @param insertChange How much energy was inserted on the last tick, or `null` where that is not being shown. Defaults to `null`.
     * @param insertAverage How much energy has been inserted per tick lately, or `null` where that is not being shown. Defaults to `null`.
     * @param extractChange How much energy was extracted on the last tick, or `null` where that is not being shown. Defaults to `null`.
     * @param extractAverage How much energy has been extracted per tick lately, or `null` where that is not being shown. Defaults to `null`.
     * @param averageDuration The unit the averages are taken over, as written in the tooltip. Defaults to `s`, i.e. one second.
     * @param advanced Whether the tooltip is expanded, or `null` where it is not being expanded at all. Defaults to `null`.
     * @return The lines of the tooltip.
     * @since 0.6.0
     */
    fun getEnergyInfo(energy: Long, capacity: Long, maxInput: Long, maxOutput: Long, insertChange: Long? = null, insertAverage: Long? = null, extractChange: Long? = null, extractAverage: Long? = null, averageDuration: String = "s", advanced: Boolean? = null): List<MutableComponent> {
        val ret = mutableListOf(
            createPropertyComponent("energy", "stored", TextColor.YELLOW.value,
                createSIComponent(energy, number1rdp, "-m", forceUnit = (advanced ?: false).orNull("")),
                createSIComponent(capacity, number1rdp, "-m", forceUnit = (advanced ?: false).orNull(""))),
        )

        ret += getEnergyMaxInfo(maxInput, maxOutput, advanced)
        ret += getEnergyChangeInfo(insertChange, extractChange, insertAverage, extractAverage, averageDuration, advanced)
        ret += getEnergyDurationInfo(insertAverage, extractAverage, insertChange, extractChange, capacity, energy)

        if (advanced == false) {
            ret.add(getAdvancedPrompt())
        }

        return ret
    }

    /**
     * The line saying how long something has until it fills or empties, worked out from what it is currently gaining or losing per tick.
     * Nothing is said where it is neither gaining nor losing, since it would never arrive.
     *
     * @param insertAverage How much energy has been inserted per tick lately, or `null` where that is not known.
     * @param extractAverage How much energy has been extracted per tick lately, or `null` where that is not known.
     * @param insertChange How much energy was inserted on the last tick, or `null` where that is not known.
     * @param extractChange How much energy was extracted on the last tick, or `null` where that is not known.
     * @param capacity How much energy can be stored.
     * @param energy How much energy is stored.
     * @return The lines of the tooltip, which is empty where nothing is changing.
     * @since 0.8.0
     */
    fun getEnergyDurationInfo(insertAverage: Long?, extractAverage: Long?, insertChange: Long?, extractChange: Long?, capacity: Long, energy: Long): List<MutableComponent> {
        val ret = mutableListOf<MutableComponent>()
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
        return ret
    }

    /**
     * The lines saying what energy has moved lately.
     * An expanded tooltip separates what came in from what went out and adds the average of the two, while a collapsed one gives only the net figure.
     *
     * @param insertChange How much energy was inserted on the last tick, or `null` where that is not being shown.
     * @param extractChange How much energy was extracted on the last tick, or `null` where that is not being shown.
     * @param insertAverage How much energy has been inserted per tick lately, or `null` where that is not being shown.
     * @param extractAverage How much energy has been extracted per tick lately, or `null` where that is not being shown.
     * @param averageDuration The unit the averages are taken over, as written in the tooltip.
     * @param advanced Whether the tooltip is expanded, or `null` where it is not being expanded at all. Defaults to `null`.
     * @return The lines of the tooltip.
     * @since 0.8.0
     */
    fun getEnergyChangeInfo(insertChange: Long?, extractChange: Long?, insertAverage: Long?, extractAverage: Long?, averageDuration: String, advanced: Boolean? = null): List<MutableComponent> {
        val ret = mutableListOf<MutableComponent>()
        if (advanced == true) {
            if (insertChange != null) {
                ret.add(createPropertyComponent("energy", "insert", 0xFFDAFF7F.toInt(), createPlusComponent(insertChange, createSIComponent(insertChange, number1rdp, "-m", forceUnit = ""))))
            }
            if (extractChange != null) {
                ret.add(createPropertyComponent("energy", "extract", 0xFFFFB27F.toInt(), createSIComponent(-extractChange, number1rdp, "-m", forceUnit = "")))
            }
            if (insertAverage != null || extractAverage != null) {
                val average = (insertAverage ?: 0L) - (extractAverage ?: 0L)
                ret.add(
                    createPropertyComponent(
                        "energy", "change.avg", when {
                            average > 0L -> 0xFFDAFF7F.toInt()
                            average < 0L -> 0xFFFFB27F.toInt()
                            else -> 0xFFBBAF76.toInt()
                        }, createPlusComponent(average, createSIComponent(average, number1rdp, "-m", forceUnit = "")), averageDuration
                    )
                )
            }
        } else {
            if (insertChange != null || extractChange != null) {
                val change = (insertChange ?: 0L) - (extractChange ?: 0L)
                ret.add(
                    createPropertyComponent(
                        "energy", "change", when {
                            change > 0L -> 0xFFDAFF7F.toInt()
                            change < 0L -> 0xFFFFB27F.toInt()
                            else -> 0xFFBBAF76.toInt()
                        }, createPlusComponent(change, createSIComponent(change, number1rdp, "-m"))
                    )
                )
            }
        }
        return ret
    }

    /**
     * The lines saying how much energy can move per tick.
     * Where the two limits are the same they are written as one line, unless the tooltip is expanded, in which case each is always named separately.
     *
     * @param maxInput How much energy can be inserted per tick.
     * @param maxOutput How much energy can be extracted per tick.
     * @param advanced Whether the tooltip is expanded, or `null` where it is not being expanded at all. Defaults to `null`.
     * @return The lines of the tooltip, which is empty where nothing can move at all.
     * @since 0.8.0
     */
    fun getEnergyMaxInfo(maxInput: Long, maxOutput: Long, advanced: Boolean? = null): List<MutableComponent> {
        val ret = mutableListOf<MutableComponent>()
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
        return ret
    }

    /**
     * The block of lines describing what an item holds of To Energy, read from the component the item carries it in.
     *
     * @param data The energy the item is carrying.
     * @param advanced Whether the tooltip is expanded, or `null` where it is not being expanded at all. Defaults to `null`.
     * @return The lines of the tooltip.
     * @since 0.6.0
     */
    fun getEnergyItemInfo(data: ToEnergyItemComponentData, advanced: Boolean? = null) = getEnergyInfo(data.energy, data.max, data.maxInput, data.maxOutput, advanced = advanced)

    /**
     * The line telling the player which key expands the tooltip, shown while it is still collapsed.
     *
     * @return The line of the tooltip.
     * @since 0.7.0
     */
    fun getAdvancedPrompt() = Component.translatable("tooltip.$modid.more", Component.keybind("key.$modid.keyboard.shift").withStyle(Style.EMPTY.withBold(true)))
        .withStyle(Style.EMPTY.withColor(0xFF562CCB.toInt()))

}
