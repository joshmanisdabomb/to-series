package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.resources.Identifier
import net.minecraft.world.inventory.Slot

class EnergyTransferFromStackWidget(slot: Slot, val storageFull: () -> Boolean, x: Int, y: Int, width: Int = 9, height: Int = 10, base: Identifier = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/download_")) : EnergyTransferStackWidget(slot, base, x, y, width, height) {

    override fun getIconTexture(energy: ToEnergyTransferContext?) = when {
        slot.item.isEmpty -> empty
        energy == null -> block
        energy.getTotalAmount(Unit) <= 0 -> done
        storageFull() -> block
        else -> full
    }

}
