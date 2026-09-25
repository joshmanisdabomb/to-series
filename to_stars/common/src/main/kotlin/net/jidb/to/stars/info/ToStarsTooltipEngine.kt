package net.jidb.to.stars.info

import net.jidb.to.base.api.helper.KotlinHelper.orNull
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.pub.block.ToEnergyCableBlock
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.createDurationComponent
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.createPlusComponent
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.createSIComponent
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.getAdvancedPrompt
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.LossyToEnergyCableBlock
import net.jidb.to.stars.entity.AtomicBombEntity
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack

/**
 * [TooltipEngine] implementation for the content mod, which is where the tooltips describing what each of its machines can do are written.
 *
 * Most of it takes an `advanced` flag, which is `null` where a tooltip is not being expanded at all, `false` while it is collapsed and `true` once the player holds shift.
 * A collapsed tooltip gives only the figures that matter to playing, while an expanded one also gives what those figures were worked out from.
 */
object ToStarsTooltipEngine : TooltipEngine(ToStarsMod.modid) {

    /**
     * The lines describing an atomic bomb: how strong the blast will be and how long the fuse burns, both of which follow from how much uranium it was loaded with.
     *
     * @param uranium How much uranium the bomb holds.
     * @return The lines of the tooltip.
     */
    fun getAtomicBombInfo(uranium: Int) = listOf(
        createPropertyComponent("atomic_bomb", "strength", TextColor.GREEN.value, AtomicBombEntity.getExplosionStrength(uranium)),
        createPropertyComponent("atomic_bomb", "fuse", TextColor.YELLOW.value, AtomicBombEntity.getFuseTime(uranium) / 20)
    )

    /**
     * The line naming a machine's tier.
     *
     * @param tier The tier of the machine.
     * @return The lines of the tooltip.
     */
    fun getMachineInfo(tier: MachineTier) = listOf(
        createPropertyComponent("machine", "tier", tier.chatColor, number1dp.format(tier.number))
    )

    /**
     * The lines describing what a generator can do, worked out from the figures themselves rather than from a tier, so that a generator whose figures do not come from one can still be described.
     *
     * @param value How much heat is made per tick from a fuel of ordinary value.
     * @param speed How long the generator takes over its fuel, as a multiple.
     * @param initial The heat it settles at while doing nothing.
     * @param range How far above that it can be driven.
     * @param bonus The extra heat it is given for running at its limit.
     * @param cooling What is left of its heat each tick once it stops being fed.
     * @return The lines of the tooltip.
     */
    fun getGeneratorInfo(value: Float, speed: Float, initial: Float, range: Float, bonus: Float, cooling: Float) = listOf(
        createPropertyComponent("generator", "heat", TextColor.GOLD.value, number3dp.format(value * 20)),
        createPropertyComponent("generator", "speed", (if (speed < 1f) TextColor.RED else TextColor.WHITE).value, number2dp.format(100 / speed)),
        createPropertyComponent("generator", "initial", 0xFFCDAE6F.toInt(), number1dp.format(initial)),
        createPropertyComponent("generator", "range", 0xFFCDAE6F.toInt(), number1dp.format(initial + range)),
        createPropertyComponent("generator", "bonus", TextColor.YELLOW.value, number1dp.format(bonus)),
        createPropertyComponent("generator", "cooling", TextColor.RED.value, number2dp.format((1 - cooling) * -100)),
    )

    /**
     * The lines describing what a generator of a given tier can do.
     *
     * @param tier The tier of the generator.
     * @return The lines of the tooltip.
     */
    fun getGeneratorInfo(tier: MachineTier) = getGeneratorInfo(tier.generatorHeat, tier.machineSpeed, tier.generatorInitial, tier.generatorRange, tier.generatorBonus, tier.generatorCooling)

    /**
     * The lines describing how hot a generator currently is: what it is at, what it is heading for, how fast it is changing and how long it has until it gets there.
     *
     * Nothing is said about where it is heading once it is past its limit, since it is no longer heading anywhere.
     *
     * @param heat How hot the generator is.
     * @param target How hot it is heading for.
     * @param change How much its heat is changing per tick.
     * @param value How much heat is made per tick from a fuel of ordinary value.
     * @param speed How long it takes over its fuel, as a multiple.
     * @param initial The heat it settles at while doing nothing.
     * @param range How far above that it can be driven.
     * @param bonus The extra heat it is given for running at its limit.
     * @param cooling What is left of its heat each tick once it stops being fed.
     * @param advanced Whether the tooltip is expanded, or `null` where it is not being expanded at all. Defaults to `null`.
     * @return The lines of the tooltip.
     */
    fun getGeneratorHeatInfo(heat: Float, target: Float, change: Float, value: Float, speed: Float, initial: Float, range: Float, bonus: Float, cooling: Float, advanced: Boolean? = null): List<Component> {
        val ret = mutableListOf(
            createPropertyComponent("generator", "current", (if (heat > initial + range) TextColor.YELLOW else TextColor.GOLD).value, number1dp.format(heat)),
        )
        if (target != heat && heat <= initial + range) {
            ret.add(createPropertyComponent("generator", "target", (if (target <= 0f) TextColor.RED else TextColor.WHITE).value, number1dp.format(target)))
        }

        ret.add(createPropertyComponent("generator", "change", when {
            change > 0L -> TextColor.GREEN
            change < 0L -> TextColor.RED
            else -> TextColor.WHITE
        }.value, createPlusComponent(change, number2dp.format(change * 20))))
        if (heat <= initial + range) {
            if (change > 0L) {
                val ticks = (initial + range - heat) / change
                ret.add(createPropertyComponent("generator", "result.full", TextColor.WHITE.value, createDurationComponent(ticks.toLong())))
            } else if (change < 0L) {
                val ticks = (heat - initial) / -change
                ret.add(createPropertyComponent("generator", "result.empty", TextColor.WHITE.value, createDurationComponent(ticks.toLong())))
            }
        }

        val generator = getGeneratorInfo(value, speed, initial, range, bonus, cooling)
        return ret + generator.subList(if (advanced == true) 0 else 2, generator.count() - (if (advanced == true) 0 else 1)) + listOfNotNull(if (advanced == false) getAdvancedPrompt() else null)
    }

    /**
     * The lines describing what a fuel is worth to a generator: the heat it makes, how long it lasts, what the whole item and the whole stack come to, and how much of a machine's range they would fill.
     *
     * @param stack The fuel being described, whose count decides whether the stack total is worth saying.
     * @param value How much this fuel is worth against an ordinary one.
     * @param duration How long the fuel burns for, in ticks, before the generator's own speed applies.
     * @param target How hot the generator is heading for.
     * @param max How hot it can be driven.
     * @param mult How much heat the generator makes per tick from a fuel of ordinary value.
     * @param speed How long the generator takes over its fuel, as a multiple.
     * @param advanced Whether the tooltip is expanded, or `null` where it is not being expanded at all. Defaults to `null`.
     * @return The lines of the tooltip.
     */
    fun getGeneratorFuelInfo(stack: ItemStack, value: Float, duration: Short, target: Float, max: Float, mult: Float, speed: Float, advanced: Boolean? = null): List<Component> {
        val ret = mutableListOf<Component>()
        val total = duration * speed * value * mult

        ret.add(createPropertyComponent("generator", "fuel.heat", TextColor.GOLD.value, number3dp.format(value * mult * 20), if (advanced != true) Component.translatable("tooltip.$modid.generator.fuel.multiplier.short", number1dp.format(value))
            .withStyle(Style.EMPTY.withColor(if (value > 1f) 0xFF4287f5.toInt() else TextColor.GRAY.value)) else Component.empty()))
        if (advanced == true) {
            ret.add(createPropertyComponent("generator", "fuel.multiplier", if (value > 1f) 0xFF4287f5.toInt() else TextColor.WHITE.value, number0dp.format(value * 100)))
        }

        ret.add(createPropertyComponent("generator", "fuel.duration", TextColor.WHITE.value, createDurationComponent((duration * speed).toLong())))
        if (advanced == true) {
            ret.add(createPropertyComponent("generator", "fuel.duration.base", TextColor.WHITE.value, createDurationComponent(duration.toLong())))
        }

        ret.add(createPropertyComponent("generator", "fuel.total", TextColor.YELLOW.value, number2dp.format(total)))
        if (stack.count > 1) {
            ret.add(createPropertyComponent("generator", "fuel.stack", TextColor.YELLOW.value, number2dp.format(total * stack.count), Component.translatable("tooltip.$modid.generator.fuel.count", stack.count)
                .withStyle(Style.EMPTY.withColor(TextColor.WHITE.value))))
        }

        if (target < max) {
            ret.add(createPropertyComponent("generator", "fuel.fill", TextColor.WHITE.value, number1dp.format((max - target) / total)))
        }
        if (advanced == true) {
            ret.add(createPropertyComponent("generator", "fuel.fill.base", TextColor.WHITE.value, number1dp.format(max / total)))
        }

        return ret + listOfNotNull(if (advanced == false) getAdvancedPrompt() else null)
    }

    /**
     * The lines describing what a generator is burning at this moment, i.e. what it is gaining per tick and how much of the fuel is left.
     *
     * @param add How much heat is being made per tick.
     * @param remaining How long the current fuel has left, in ticks.
     * @return The lines of the tooltip.
     */
    fun getGeneratorBurnInfo(add: Float, remaining: Short) = listOfNotNull(
        createPropertyComponent("generator", "change", when {
            add > 0L -> TextColor.GREEN
            add < 0L -> TextColor.RED
            else -> TextColor.WHITE
        }.value, createPlusComponent(add, number2dp.format(add * 20))),
        if (remaining > 0) createPropertyComponent("generator", "remaining", TextColor.WHITE.value, createDurationComponent(remaining.toLong())) else null
    )

    /**
     * The line describing how much energy a turbine of a given tier makes.
     *
     * @param tier The tier of the turbine.
     * @return The lines of the tooltip.
     */
    fun getTurbineInfo(tier: MachineTier) = listOf(
        createPropertyComponent("turbine", "rate", TextColor.GOLD.value, createSIComponent((1000 * tier.turbineRate).toLong(), number1dp, "-m")),
    )

    /**
     * The lines describing what a processor of a given tier can do: how fast it works, what it costs to run, and how much more efficient it becomes with repetition.
     *
     * @param tier The tier of the processor.
     * @return The lines of the tooltip.
     */
    fun getProcessorInfo(tier: MachineTier) = listOf(
        createPropertyComponent("processor", "speed", when {
            tier.machineSpeed > 1f -> TextColor.GREEN
            tier.machineSpeed < 1f -> TextColor.RED
            else -> TextColor.WHITE
        }.value, number3dp.format(tier.machineSpeed * 100)),
        createPropertyComponent("processor", "usage", when {
            tier.machineUsage < 1f -> TextColor.GREEN
            tier.machineUsage > 1f -> TextColor.RED
            else -> TextColor.WHITE
        }.value, number3dp.format(tier.machineUsage * 100)),
        createPropertyComponent("processor", "bonus", TextColor.BLUE.value, number3dp.format(tier.machineBonus.minus(1) * 100)),
        createPropertyComponent("processor", "bonus.completions", TextColor.WHITE.value, tier.machineBonusMax),
        createPropertyComponent("processor", "bonus.max", 0xFF00FFFF.toInt(), number3dp.format(tier.machineBonus.minus(1).times(tier.machineBonusMax).plus(1) * 100)),
    )

    /**
     * The lines describing how efficient a machine has become at the recipe it is currently running, which is what it gains by being left on the same one.
     *
     * @param currentRecipe The name of the recipe it is set to, or `null` where it is set to none.
     * @param incorrect Whether what is in the machine does not match the recipe it is set to, which loses the efficiency it had built up.
     * @param completions How many times it has run that recipe.
     * @param bonus How much more efficient each run makes it.
     * @param max How many runs it keeps gaining over.
     * @return The lines of the tooltip.
     */
    fun getEfficiencyInfo(currentRecipe: Component?, incorrect: Boolean, completions: Int, bonus: Float, max: Int) = listOfNotNull(
        if (currentRecipe != null) {
            createPropertyComponent("efficiency", "recipe", TextColor.GREEN.value, currentRecipe)
        } else {
            createPropertyComponent("efficiency", "recipe", TextColor.DARK_GRAY.value, ToBaseTooltipEngine.none)
        },
        if (incorrect) Component.translatable("tooltip.$modid.efficiency.incorrect").withStyle(Style.EMPTY.withColor(TextColor.RED.value)) else null,
        createPropertyComponent("efficiency", "current", 0xFF00CCFF.toInt(), number3dp.format(bonus.minus(1).times(completions).plus(1) * 100)),
        createPropertyComponent("efficiency", "bonus", TextColor.BLUE.value, number3dp.format(bonus.minus(1) * 100)),
        createPropertyComponent("efficiency", "completions", TextColor.WHITE.value, completions, max),
        createPropertyComponent("efficiency", "max", 0xFF00FFFF.toInt(), number3dp.format(bonus.minus(1).times(max).plus(1) * 100)),
    )

    /**
     * The lines describing where a processor is with its recipe: how long it has left, what the whole recipe takes, and what it costs both per tick and in total.
     *
     * Where the recipe would take no time and cost nothing, one line saying so is given instead, since the machine has clearly been set to something it cannot run.
     *
     * @param progress How far through the recipe it is, in ticks.
     * @param max How long the recipe takes it, in ticks.
     * @param time How long the recipe itself asks for, in ticks.
     * @param energy How much energy the recipe itself asks for.
     * @param usage How much energy the machine spends, as a multiple.
     * @param speed How long the machine takes, as a multiple.
     * @param advanced Whether the tooltip is expanded, or `null` where it is not being expanded at all. Defaults to `null`.
     * @return The lines of the tooltip.
     */
    fun getProcessorProgressInfo(progress: Short, max: Short, time: Int, energy: Long, usage: Float, speed: Float, advanced: Boolean? = null): List<MutableComponent> {
        val remaining = max - progress
        val total = Mth.ceil(energy * usage)
        val cost = Mth.ceil(total * speed / time)
        val totalForMax = cost * max
        val base = Mth.ceil(energy / time.toFloat())
        val initial = listOfNotNull(
            if (remaining > 0) createPropertyComponent("processor", "remaining", TextColor.WHITE.value, createDurationComponent(remaining.toLong())) else null,
            if (max > 0) createPropertyComponent("processor", "max", 0xFFDDEEFF.toInt(), createDurationComponent(max.toLong())) else null,
            if (energy > 0) createPropertyComponent("processor", "energy", 0xFFFFB27F.toInt(), createSIComponent(cost.toLong(), number1rdp, "-m", forceUnit = (advanced ?: false).orNull(""))) else null,
            if (energy > 0) createPropertyComponent("processor", "energy.total", TextColor.YELLOW.value, createSIComponent(totalForMax.toLong(), number1rdp, "-m", forceUnit = (advanced ?: false).orNull(""))) else null,
            if (advanced == true && time > 0) createPropertyComponent("processor", "max.base", 0xFFDDEEFF.toInt(), createDurationComponent(time.toLong())) else null,
            if (advanced == true && energy > 0) createPropertyComponent("processor", "energy.base", 0xFFFFB27F.toInt(), createSIComponent(base.toLong(), number1rdp, "-m", forceUnit = "")) else null,
            if (advanced == true && energy > 0) createPropertyComponent("processor", "energy.total.base", TextColor.YELLOW.value, createSIComponent(energy, number1rdp, "-m", forceUnit = "")) else null,
        )
        if (initial.isEmpty()) {
            return listOf(Component.translatable("tooltip.$modid.processor.invalid").withStyle(Style.EMPTY.withItalic(true).withColor(0xFFFFAAAA.toInt())))
        }
        return initial + listOfNotNull(if (advanced == false) getAdvancedPrompt() else null)
    }

    /**
     * The line describing how much energy is lost across a cable, which is nothing at all for one that loses none.
     *
     * @param cable The cable being described.
     * @return The lines of the tooltip.
     */
    fun getPowerCableInfo(cable: ToEnergyCableBlock): List<MutableComponent> {
        val loss = (cable as? LossyToEnergyCableBlock)?.loss ?: 0f
        return listOf(
            createPropertyComponent("power_cable", "loss", (if (loss > 0f) TextColor.RED else TextColor.AQUA).value, number2dp.format(loss * 100))
        )
    }

}
