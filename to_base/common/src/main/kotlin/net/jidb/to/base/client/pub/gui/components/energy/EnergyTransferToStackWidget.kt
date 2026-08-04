package net.jidb.to.base.client.pub.gui.components.energy

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.minecraft.resources.Identifier
import net.minecraft.world.inventory.Slot

/**
 * The [EnergyTransferStackWidget] for energy being pushed out of the block holding the screen and into the item in the slot.
 * The arrow is only shown as able to move where the item still has room and the block has something to give.
 *
 * @param slot The slot the arrow is drawn next to, whose item is read to decide which icon to draw.
 * @property storageEmpty A function saying whether the block being drawn from has run out of energy.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @param width The width of the widget. Defaults to `9`.
 * @param height The height of the widget. Defaults to `10`.
 * @param base The prefix each of the four icon textures is named after. Defaults to the upload arrows of To Lay the Foundations.
 * @since 0.6.0
 */
class EnergyTransferToStackWidget(slot: Slot, val storageEmpty: () -> Boolean, x: Int, y: Int, width: Int = 9, height: Int = 10, base: Identifier = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/upload_")) : EnergyTransferStackWidget(slot, base, x, y, width, height) {

    override fun getIconTexture(energy: ToEnergyTransferContext?) = when {
        slot.item.isEmpty -> empty
        energy == null -> block
        energy.getTotalAmount(Unit) >= energy.getTotalCapacity(Unit) -> done
        storageEmpty() -> block
        else -> full
    }

}
