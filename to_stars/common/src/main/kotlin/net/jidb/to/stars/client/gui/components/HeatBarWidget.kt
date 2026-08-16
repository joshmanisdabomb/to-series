package net.jidb.to.stars.client.gui.components

import net.jidb.to.base.client.pub.gui.components.AbstractBarWidget
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.HeatGeneratorMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import net.minecraft.world.inventory.ContainerData

class HeatBarWidget(private val data: ContainerData, x: Int, y: Int) : AbstractBarWidget(x, y, 120, 16) {

    override fun getFill(): Float {
        val raw = HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.HEAT) ?: 0f
        val heat = raw - (HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.INITIAL) ?: 0f)

        val max = HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.RANGE) ?: 0f
        if (max <= 0) return 0f

        val fill = heat / max
        return (bulb + Mth.floor(fill * width.minus(bulb))) / width.toFloat()
    }

    fun getGhostFill(): Float {
        val raw = HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.HEAT) ?: 0f
        val heat = raw - (HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.INITIAL) ?: 0f)
        val target = heat + (HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.ADD) ?: 0f).times(HeatGeneratorMenu.dataSchema.getShortValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.DURATION) ?: 0)
        val max = HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.RANGE) ?: 0f
        if (max <= 0) return 0f
        val fill = target.coerceAtMost(max) / max
        return (bulb + Mth.floor(fill * width.minus(bulb))) / width.toFloat()
    }

    override fun getBackTexture() = back
    override fun getFrontTexture(): Identifier {
        val fill = getFill()
        return if (fill >= 1f) max else front
    }

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a)
        val fx = Mth.ceil(getFill() * width)
        val gx = Mth.ceil(getGhostFill() * width)
        if (fx < width && gx > fx) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Companion.ghost, width, height, fx, 0, x + fx, y, gx - fx, height, ARGB.white(alpha))
        }
    }

    override fun createNarrationMessage() = Component.empty()
    /*.translatable(
        "tooltip.${ToBaseMod.modid}.energy.stored.value",
        ToBaseTooltipEngine.createSIComponent(energy(), 1, Minecraft.getInstance().hasShiftDown()),
        ToBaseTooltipEngine.createSIComponent(capacity(), 1, Minecraft.getInstance().hasShiftDown())
    )*/

    companion object {

        const val bulb = 13

        val back = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_empty")

        val ghost = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_ghost")

        val front = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_full")

        val max = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_max")

    }

}
