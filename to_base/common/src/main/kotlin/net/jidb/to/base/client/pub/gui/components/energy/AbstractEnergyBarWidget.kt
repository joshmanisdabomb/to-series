package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.KotlinHelper.orNull
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.pub.gui.components.AbstractBarWidget
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

/**
 * An [AbstractBarWidget] that draws how much energy something is holding against how much it can hold.
 * The narration reads out both figures with an SI prefix, and holding shift shows the exact numbers instead, matching how energy is written in a tooltip.
 *
 * @property energy A function supplying how much energy is currently stored.
 * @property capacity A function supplying how much energy can be stored.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @param width The width of the widget, which is also the width of both textures.
 * @param height The height of the widget, which is also the height of both textures.
 * @since 0.8.0
 */
abstract class AbstractEnergyBarWidget(val energy: () -> Long, val capacity: () -> Long, x: Int, y: Int, width: Int, height: Int) : AbstractBarWidget(x, y, width, height) {

    override fun getFill() = (energy() / capacity().toFloat())

    override fun createNarrationMessage() = Component.translatable(
        "tooltip.${ToBaseMod.modid}.energy.stored.value",
        ToBaseTooltipEngine.createSIComponent(energy(), TooltipEngine.number1rdp, "-m", forceUnit = Minecraft.getInstance().hasShiftDown().orNull("")),
        ToBaseTooltipEngine.createSIComponent(capacity(), TooltipEngine.number1rdp, "-m", forceUnit = Minecraft.getInstance().hasShiftDown().orNull(""))
    )

}
