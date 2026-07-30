package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.KotlinHelper.orNull
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.pub.gui.components.AbstractBarWidget
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

abstract class AbstractEnergyBarWidget(val energy: () -> Long, val capacity: () -> Long, x: Int, y: Int, width: Int, height: Int) : AbstractBarWidget(x, y, width, height) {

    override fun getFill() = (energy() / capacity().toFloat())

    override fun createNarrationMessage() = Component.translatable(
        "tooltip.${ToBaseMod.modid}.energy.stored.value",
        ToBaseTooltipEngine.createSIComponent(energy(), TooltipEngine.number1rdp, "-m", forceUnit = Minecraft.getInstance().hasShiftDown().orNull("")),
        ToBaseTooltipEngine.createSIComponent(capacity(), TooltipEngine.number1rdp, "-m", forceUnit = Minecraft.getInstance().hasShiftDown().orNull(""))
    )

}