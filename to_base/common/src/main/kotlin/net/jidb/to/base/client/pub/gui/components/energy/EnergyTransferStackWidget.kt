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

abstract class EnergyTransferStackWidget(protected val slot: Slot, protected val base: Identifier, x: Int, y: Int, width: Int = 9, height: Int = 10) : AbstractWidget(x, y, width, height, Component.empty()) {

    val empty = base.withSuffix("empty")
    val full = base.withSuffix("full")
    val block = base.withSuffix("block")
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

    abstract fun getIconTexture(energy: ToEnergyTransferContext?): Identifier

    override fun createNarrationMessage() = Component.empty()
    override fun updateWidgetNarration(output: NarrationElementOutput) = Unit
    override fun isValidClickButton(buttonInfo: MouseButtonInfo) = false

}
