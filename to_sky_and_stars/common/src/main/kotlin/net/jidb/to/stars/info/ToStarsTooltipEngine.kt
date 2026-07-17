package net.jidb.to.stars.info

import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.pub.block.ToEnergyCableBlock
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.createDurationComponent
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.createPlusComponent
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.getAdvancedPrompt
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.LossyToEnergyCableBlock
import net.jidb.to.stars.entity.AtomicBombEntity
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.ItemStack

object ToStarsTooltipEngine : TooltipEngine(ToStarsMod.modid) {

    fun getAtomicBombInfo(uranium: Int) = listOf(
        createPropertyComponent("atomic_bomb", "strength", TextColor.GREEN.value, AtomicBombEntity.getExplosionStrength(uranium)),
        createPropertyComponent("atomic_bomb", "fuse", TextColor.YELLOW.value, AtomicBombEntity.getFuseTime(uranium) / 20)
    )

    fun getMachineInfo(tier: MachineTier) = listOf(
        createPropertyComponent("machine", "tier", tier.chatColor, number1dp.format(tier.number))
    )

    fun getGeneratorInfo(value: Float, speed: Float, initial: Float, range: Float, bonus: Float, cooling: Float) = listOf(
        createPropertyComponent("generator", "heat", TextColor.GOLD.value, number3dp.format(value * 20)),
        createPropertyComponent("generator", "speed", (if (speed < 1f) TextColor.RED else TextColor.WHITE).value, number2dp.format(100 / speed)),
        createPropertyComponent("generator", "initial", 0xFFCDAE6F.toInt(), number1dp.format(initial)),
        createPropertyComponent("generator", "range", 0xFFCDAE6F.toInt(), number1dp.format(initial + range)),
        createPropertyComponent("generator", "bonus", TextColor.YELLOW.value, number1dp.format(bonus)),
        createPropertyComponent("generator", "cooling", TextColor.RED.value, number2dp.format((1 - cooling) * -100)),
    )

    fun getGeneratorInfo(tier: MachineTier) = getGeneratorInfo(tier.generatorHeat, tier.machineSpeed, tier.generatorInitial, tier.generatorRange, tier.generatorBonus, tier.generatorCooling)

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

    fun getGeneratorBurnInfo(add: Float, remaining: Short) = listOfNotNull(
        createPropertyComponent("generator", "change", when {
            add > 0L -> TextColor.GREEN
            add < 0L -> TextColor.RED
            else -> TextColor.WHITE
        }.value, createPlusComponent(add, number2dp.format(add * 20))),
        if (remaining > 0) createPropertyComponent("generator", "remaining", TextColor.WHITE.value, createDurationComponent(remaining.toLong())) else null
    )

    fun getTurbineInfo(tier: MachineTier) = listOf(
        createPropertyComponent("turbine", "rate", TextColor.GOLD.value, ToBaseTooltipEngine.createSIComponent((1000 * tier.turbineRate).toLong(), number1dp, "-m")),
    )

    fun getPowerCableInfo(cable: ToEnergyCableBlock): List<MutableComponent> {
        val loss = (cable as? LossyToEnergyCableBlock)?.loss ?: 0f
        return listOf(
            createPropertyComponent("power_cable", "loss", (if (loss > 0f) TextColor.RED else TextColor.AQUA).value, number2dp.format(loss * 100))
        )
    }

}