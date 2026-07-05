package net.jidb.to.stars.info

import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.pub.block.ToEnergyCableBlock
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.createDurationComponent
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.createPlusComponent
import net.jidb.to.base.pub.info.ToBaseTooltipEngine.getAdvancedPrompt
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.LossyToEnergyCableBlock
import net.jidb.to.stars.entity.AtomicBombEntity
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack

object ToStarsTooltipEngine : TooltipEngine(ToStarsMod.modid) {

    fun getAtomicBombInfo(uranium: Int) = listOf(
        createPropertyComponent("atomic_bomb", "strength", ChatFormatting.GREEN.color!!, AtomicBombEntity.getExplosionStrength(uranium)),
        createPropertyComponent("atomic_bomb", "fuse", ChatFormatting.YELLOW.color!!, AtomicBombEntity.getFuseTime(uranium) / 20)
    )

    fun getMachineInfo(tier: MachineTier) = listOf(
        createPropertyComponent("machine", "tier", tier.chatColor, number1dp.format(tier.number))
    )

    fun getGeneratorInfo(value: Float, speed: Float, initial: Float, range: Float, bonus: Float, cooling: Float) = listOf(
        createPropertyComponent("generator", "heat", ChatFormatting.GOLD.color!!, number3dp.format(value * 20)),
        createPropertyComponent("generator", "speed", (if (speed < 1f) ChatFormatting.RED else ChatFormatting.WHITE).color!!, number2dp.format(100 / speed)),
        createPropertyComponent("generator", "initial", 0xFFCDAE6F.toInt(), number1dp.format(initial)),
        createPropertyComponent("generator", "range", 0xFFCDAE6F.toInt(), number1dp.format(initial + range)),
        createPropertyComponent("generator", "bonus", ChatFormatting.YELLOW.color!!, number1dp.format(bonus)),
        createPropertyComponent("generator", "cooling", ChatFormatting.RED.color!!, number2dp.format((1 - cooling) * -100)),
    )

    fun getGeneratorInfo(tier: MachineTier) = getGeneratorInfo(tier.generatorHeat, tier.machineSpeed, tier.generatorInitial, tier.generatorRange, tier.generatorBonus, tier.generatorCooling)

    fun getGeneratorHeatInfo(heat: Float, target: Float, change: Float, value: Float, speed: Float, initial: Float, range: Float, bonus: Float, cooling: Float, advanced: Boolean? = null): List<Component> {
        val ret = mutableListOf(
            createPropertyComponent("generator", "current", (if (heat > initial + range) ChatFormatting.YELLOW else ChatFormatting.GOLD).color!!, number1dp.format(heat)),
        )
        if (target != heat && heat <= initial + range) {
            ret.add(createPropertyComponent("generator", "target", (if (target <= 0f) ChatFormatting.RED else ChatFormatting.WHITE).color!!, number1dp.format(target)))
        }

        ret.add(createPropertyComponent("generator", "change", when {
            change > 0L -> ChatFormatting.GREEN
            change < 0L -> ChatFormatting.RED
            else -> ChatFormatting.WHITE
        }.color!!, createPlusComponent(change, number2dp.format(change * 20))))
        if (heat <= initial + range) {
            if (change > 0L) {
                val ticks = (initial + range - heat) / change
                ret.add(createPropertyComponent("generator", "result.full", ChatFormatting.WHITE.color!!, createDurationComponent(ticks.toLong())))
            } else if (change < 0L) {
                val ticks = (heat - initial) / -change
                ret.add(createPropertyComponent("generator", "result.empty", ChatFormatting.WHITE.color!!, createDurationComponent(ticks.toLong())))
            }
        }

        val generator = getGeneratorInfo(value, speed, initial, range, bonus, cooling)
        return ret + generator.subList(if (advanced == true) 0 else 2, generator.count() - (if (advanced == true) 0 else 1)) + listOfNotNull(if (advanced == false) getAdvancedPrompt() else null)
    }

    fun getGeneratorFuelInfo(stack: ItemStack, value: Float, duration: Short, target: Float, max: Float, mult: Float, speed: Float, advanced: Boolean? = null): List<Component> {
        val ret = mutableListOf<Component>()
        val total = duration * speed * value * mult

        ret.add(createPropertyComponent("generator", "fuel.heat", ChatFormatting.GOLD.color!!, number3dp.format(value * mult * 20), if (advanced != true) Component.translatable("tooltip.$modid.generator.fuel.multiplier.short", number1dp.format(value))
            .withStyle(Style.EMPTY.withColor(if (value > 1f) 0xFF4287f5.toInt() else ChatFormatting.GRAY.color!!)) else Component.empty()))
        if (advanced == true) {
            ret.add(createPropertyComponent("generator", "fuel.multiplier", if (value > 1f) 0xFF4287f5.toInt() else ChatFormatting.WHITE.color!!, number0dp.format(value * 100)))
        }

        ret.add(createPropertyComponent("generator", "fuel.duration", ChatFormatting.WHITE.color!!, createDurationComponent((duration * speed).toLong())))
        if (advanced == true) {
            ret.add(createPropertyComponent("generator", "fuel.duration.base", ChatFormatting.WHITE.color!!, createDurationComponent(duration.toLong())))
        }

        ret.add(createPropertyComponent("generator", "fuel.total", ChatFormatting.YELLOW.color!!, number2dp.format(total)))
        if (stack.count > 1) {
            ret.add(createPropertyComponent("generator", "fuel.stack", ChatFormatting.YELLOW.color!!, number2dp.format(total * stack.count), Component.translatable("tooltip.$modid.generator.fuel.count", stack.count)
                .withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE.color!!))))
        }

        if (target < max) {
            ret.add(createPropertyComponent("generator", "fuel.fill", ChatFormatting.WHITE.color!!, number1dp.format((max - target) / total)))
        }
        if (advanced == true) {
            ret.add(createPropertyComponent("generator", "fuel.fill.base", ChatFormatting.WHITE.color!!, number1dp.format(max / total)))
        }

        return ret + listOfNotNull(if (advanced == false) getAdvancedPrompt() else null)
    }

    fun getGeneratorBurnInfo(add: Float, remaining: Short) = listOfNotNull(
        createPropertyComponent("generator", "change", when {
            add > 0L -> ChatFormatting.GREEN
            add < 0L -> ChatFormatting.RED
            else -> ChatFormatting.WHITE
        }.color!!, createPlusComponent(add, number2dp.format(add * 20))),
        if (remaining > 0) createPropertyComponent("generator", "remaining", ChatFormatting.WHITE.color!!, createDurationComponent(remaining.toLong())) else null
    )

    fun getPowerCableInfo(cable: ToEnergyCableBlock): List<MutableComponent> {
        val loss = (cable as? LossyToEnergyCableBlock)?.loss ?: 0f
        return listOf(
            createPropertyComponent("power_cable", "loss", (if (loss > 0f) ChatFormatting.RED else ChatFormatting.AQUA).color!!, number2dp.format(loss * 100))
        )
    }

}