package net.jidb.to.base.client.pub.gui.components

import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

class BurnBarWidget(val fill: () -> Float, x: Int, y: Int) : AbstractBarWidget(x, y, 14, 14) {

    override val animation: AnimationDirection = AnimationDirection.BOTTOM_TOP

    override fun getFill() = fill()

    override fun getBackTexture() = null
    override fun getFrontTexture() = front

    override fun createNarrationMessage() = Component.empty()

    override fun updateWidgetNarration(output: NarrationElementOutput) {
        output.add(NarratedElementType.TITLE, createNarrationMessage())
    }

    companion object {

        val front: Identifier = Identifier.withDefaultNamespace("container/furnace/lit_progress")

    }

}
