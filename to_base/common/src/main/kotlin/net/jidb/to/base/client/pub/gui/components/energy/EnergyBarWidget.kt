package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier

class EnergyBarWidget(energy: () -> Long, capacity: () -> Long, x: Int, y: Int) : AbstractEnergyBarWidget(energy, capacity, x, y, 120, 10) {

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a)
        extractScrollingStringOverContents(graphics.textRenderer(), createNarrationMessage().withStyle(Style.EMPTY.withShadowColor(0xFF550000.toInt())), 2)
    }

    override fun getBackTexture() = back
    override fun getFrontTexture() = front

    companion object {
        val front = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/bar_full")
        val back = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/bar_empty")
    }

}
