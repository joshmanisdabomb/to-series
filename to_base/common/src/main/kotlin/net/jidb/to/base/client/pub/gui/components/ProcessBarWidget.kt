package net.jidb.to.base.client.pub.gui.components

import net.minecraft.resources.Identifier

open class ProcessBarWidget(val fill: () -> Float, x: Int, y: Int) : AbstractBarWidget(x, y, 24, 16) {

    override fun getFill() = fill()

    override fun getBackTexture() = null
    override fun getFrontTexture() = front

    companion object {

        val front: Identifier = Identifier.withDefaultNamespace("container/furnace/burn_progress")

    }

}
