package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.MouseButtonInfo
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import net.minecraft.world.inventory.Slot

/**
 * A widget that draws the small arrow next to a slot, showing whether energy is moving between the item in it and the block holding the screen.
 * It is only an indicator, so it never takes a click; which of the four icons is drawn is left to the subclass, which knows the direction the energy is meant to travel in.
 *
 * @param slot The slot the arrow is drawn next to, whose item is read to decide which icon to draw.
 * @param base The prefix each of the four icon textures is named after.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @param width The width of the widget. Defaults to `9`.
 * @param height The height of the widget. Defaults to `10`.
 * @since 0.6.0
 */
abstract class EnergyTransferStackWidget(protected val slot: Slot, protected val base: Identifier, x: Int, y: Int, width: Int = 9, height: Int = 10) : AbstractWidget(x, y, width, height, Component.empty()) {

    /**
     * The icon drawn where the slot holds nothing at all.
     *
     * @since 0.6.0
     */
    val empty = base.withSuffix("empty")

    /**
     * The icon drawn where energy is able to move in the direction this widget shows.
     *
     * @since 0.6.0
     */
    val full = base.withSuffix("full")

    /**
     * The icon drawn where the item cannot take part in the transfer at all, or the other side of it has no room.
     *
     * @since 0.6.0
     */
    val block = base.withSuffix("block")

    /**
     * The icon drawn where the transfer has nothing left to do, i.e. the item is already full or already empty.
     *
     * @since 0.6.0
     */
    val done = base.withSuffix("done")

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val itemTransfer = ToBaseMod.transferProviders.items.fromContainer(slot.container, null)
        val texture = if (itemTransfer == null) {
            getIconTexture(null)
        } else {
            val energy = ToBaseMod.transferProviders.to_energy.fromItemStack(slot.item, itemTransfer, slot.index)
            getIconTexture(energy)
        }
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x, y, width, height, ARGB.white(alpha))
    }

    /**
     * Picks which of the four icons to draw for the item currently in the slot.
     *
     * @param energy The energy the item in the slot holds, or `null` where the slot's item cannot store energy at all.
     * @return The texture of the icon to draw.
     * @since 0.6.0
     */
    abstract fun getIconTexture(energy: ToEnergyTransferContext?): Identifier

    override fun createNarrationMessage() = Component.empty()
    override fun updateWidgetNarration(output: NarrationElementOutput) = Unit
    override fun isValidClickButton(buttonInfo: MouseButtonInfo) = false

}
