package net.jidb.to.stars.client.gui.components

import net.jidb.to.base.client.pub.gui.components.AbstractBarWidget
import net.jidb.to.stars.ToStarsMod
import net.minecraft.resources.Identifier

class EfficiencyBarWidget(val fill: () -> Float, x: Int, y: Int) : AbstractBarWidget(x, y, 33, 4) {

    override fun getFill() = fill()

    override fun getBackTexture() = back
    override fun getFrontTexture() = front

    companion object {

        val front = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "efficiency_full")

        val back = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "efficiency_empty")

    }

}
