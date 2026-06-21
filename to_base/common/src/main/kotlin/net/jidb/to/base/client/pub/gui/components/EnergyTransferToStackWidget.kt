package net.jidb.to.base.client.pub.gui.components

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.item.component.ToEnergyItemData
import net.minecraft.resources.Identifier
import net.minecraft.world.inventory.Slot

class EnergyTransferToStackWidget(slot: Slot, val storageEmpty: () -> Boolean, x: Int, y: Int, width: Int = 9, height: Int = 10, base: Identifier = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/upload_")) : EnergyTransferStackWidget(slot, base, x, y, width, height) {

    override fun getIconTexture(energy: ToEnergyItemData?) = when {
        slot.item.isEmpty -> empty
        energy == null -> block
        energy.energy >= energy.max -> done
        storageEmpty() -> block
        else -> full
    }

}
