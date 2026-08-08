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

/**
 * The thermometer showing how hot a generator is, drawn against the range it can be driven over rather than from nothing.
 *
 * A fainter mark is drawn ahead of the reading showing where the fuel currently burning will take it, so that a player can see whether it will be enough.
 * The bulb at the bottom is always drawn full, since a thermometer with an empty bulb reads as broken rather than as cold.
 *
 * @param data The generator's figures, as the interface reads them.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 */
class HeatBarWidget(private val data: ContainerData, x: Int, y: Int) : AbstractBarWidget(x, y, 120, 16) {

    override fun getFill(): Float {
        val raw = HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.HEAT) ?: 0f
        val heat = raw - (HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.INITIAL) ?: 0f)

        val max = HeatGeneratorMenu.dataSchema.getFloatValue(data, HeatGeneratorMenu.HeatGeneratorDataKey.RANGE) ?: 0f
        if (max <= 0) return 0f

        val fill = heat / max
        return (bulb + Mth.floor(fill * width.minus(bulb))) / width.toFloat()
    }

    /**
     * How far up the thermometer the fuel currently burning will take it, which is drawn as the fainter mark ahead of the reading.
     *
     * @return How full the bar will be, from `0` to `1`.
     */
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

        /**
         * How much of the thermometer is its bulb, which is always drawn full.
         */
        const val bulb = 13

        /**
         * The texture of the empty thermometer.
         */
        val back = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_empty")

        /**
         * The texture of the mark showing where the current fuel will take it.
         */
        val ghost = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_ghost")

        /**
         * The texture of the filled thermometer.
         */
        val front = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_full")

        /**
         * The texture of a thermometer that has been driven to its limit.
         */
        val max = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "heat/thermometer_max")

    }

}
