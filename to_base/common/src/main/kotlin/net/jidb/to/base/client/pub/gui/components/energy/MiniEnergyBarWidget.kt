package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.minecraft.resources.Identifier

class MiniEnergyBarWidget(energy: () -> Long, capacity: () -> Long, x: Int, y: Int) : AbstractEnergyBarWidget(energy, capacity, x, y, 33, 4) {

    override fun getBackTexture() = back
    override fun getFrontTexture() = front

    companion object {
        val front = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/minibar_full")
        val back = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/minibar_empty")
    }

}