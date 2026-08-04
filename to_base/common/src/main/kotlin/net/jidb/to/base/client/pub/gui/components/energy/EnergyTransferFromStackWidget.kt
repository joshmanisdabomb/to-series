package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.resources.Identifier
import net.minecraft.world.inventory.Slot

/**
 * The [EnergyTransferStackWidget] for energy being drawn out of the item in the slot and into the block holding the screen.
 * The arrow is only shown as able to move where the item still has something to give and the block has room to take it.
 *
 * @param slot The slot the arrow is drawn next to, whose item is read to decide which icon to draw.
 * @property storageFull A function saying whether the block being filled has run out of room.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @param width The width of the widget. Defaults to `9`.
 * @param height The height of the widget. Defaults to `10`.
 * @param base The prefix each of the four icon textures is named after. Defaults to the download arrows of To Lay the Foundations.
 * @since 0.6.0
 */
class EnergyTransferFromStackWidget(slot: Slot, val storageFull: () -> Boolean, x: Int, y: Int, width: Int = 9, height: Int = 10, base: Identifier = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/download_")) : EnergyTransferStackWidget(slot, base, x, y, width, height) {

    override fun getIconTexture(energy: ToEnergyTransferContext?) = when {
        slot.item.isEmpty -> empty
        energy == null -> block
        energy.getTotalAmount(Unit) <= 0 -> done
        storageFull() -> block
        else -> full
    }

}
