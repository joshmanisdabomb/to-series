package net.jidb.to.stars.info

import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.pub.block.ToEnergyCableBlock
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.LossyToEnergyCableBlock
import net.jidb.to.stars.entity.AtomicBombEntity
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.MutableComponent

object ToStarsTooltipEngine : TooltipEngine(ToStarsMod.modid) {

    fun getAtomicBombInfo(uranium: Int) = listOf(
        createPropertyComponent("atomic_bomb", "strength", ChatFormatting.GREEN.color!!, AtomicBombEntity.getExplosionStrength(uranium)),
        createPropertyComponent("atomic_bomb", "fuse", ChatFormatting.YELLOW.color!!, AtomicBombEntity.getFuseTime(uranium) / 20)
    )

    fun getMachineInfo(tier: MachineTier) = listOf(
        createPropertyComponent("machine", "tier", tier.chatColor, number1dp.format(tier.number))
    )

    fun getPowerCableInfo(cable: ToEnergyCableBlock): List<MutableComponent> {
        val loss = (cable as? LossyToEnergyCableBlock)?.loss ?: 0f
        return listOf(
            createPropertyComponent("power_cable", "loss", (if (loss > 0f) ChatFormatting.RED else ChatFormatting.AQUA).color!!, number2dp.format(loss * 100))
        )
    }

}