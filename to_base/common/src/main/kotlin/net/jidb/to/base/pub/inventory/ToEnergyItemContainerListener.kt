package net.jidb.to.base.pub.inventory

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerListener
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * A [ContainerListener] that watches the energy of the items in a menu's charging slots, so their tooltips can show how fast the energy is moving.
 * The rate cannot be read from the item itself, as an item only ever knows how much it holds now, so the change between one update and the next is remembered here instead.
 *
 * Each recorded change is kept for a tick and then cleared by [tick], so a slot that has stopped charging stops reporting a rate rather than showing the last one forever.
 *
 * @property slots The indices of the menu slots to watch.
 * @property level A function supplying the level the menu is open in, whose game time the staleness is measured against.
 * @since 0.8.0
 */
class ToEnergyItemContainerListener(val slots: IntArray, val level: () -> Level?) : ContainerListener {

    /**
     * The energy each watched slot held when it was last updated, which the next update is compared against.
     *
     * @since 0.8.0
     */
    private val lastEnergySeen = LongArray(slots.size)

    /**
     * The change in energy each watched slot last saw, which is what the tooltip reports as a rate.
     *
     * @since 0.8.0
     */
    private val lastEnergyChange = LongArray(slots.size)

    /**
     * The game time after which each watched slot's recorded change stops counting, so that a rate is not shown indefinitely.
     *
     * @since 0.8.0
     */
    private val lastEnergyStale = LongArray(slots.size)

    override fun slotChanged(menu: AbstractContainerMenu, slot: Int, stack: ItemStack) {
        if (slot !in slots) return
        val arrayIndex = slots.indexOf(slot)

        val energy = ToEnergyItemProvider.getTransferContext(stack) ?: return
        val total = energy.getTotalAmount(Unit)
        lastEnergyChange[arrayIndex] = total - lastEnergySeen[arrayIndex]
        lastEnergySeen[arrayIndex] = total
        val level = level()
        if (level != null) {
            lastEnergyStale[arrayIndex] = level.gameTime.plus(1L)
        }
    }

    /**
     * Clears the recorded change of any watched slot that has not been updated recently enough, which the menu calls each tick.
     *
     * @param menu The menu holding the watched slots.
     * @since 0.8.0
     */
    fun tick(menu: AbstractContainerMenu) {
        val tick = level()?.gameTime
        for (slot in slots) {
            val arrayIndex = slots.indexOf(slot)
            if (tick == null || tick > lastEnergyStale[arrayIndex]) {
                val energy = ToEnergyItemProvider.getTransferContext(menu.items[slot])

                lastEnergyChange[arrayIndex] = 0L
                lastEnergySeen[arrayIndex] = if (energy != null) {
                    menu.items[slot].get(ToBaseMod.itemComponents.energy_data)?.energy ?: 0L
                } else {
                    0L
                }
            }
        }
    }

    /**
     * Builds the tooltip for an item in one of the watched slots, replacing the energy lines the component would have written with ones that also report the rate.
     * The component is stripped from a copy of the stack first, so that the energy is described once by this listener rather than twice.
     *
     * @param stack The item stack to build a tooltip for.
     * @param level The client level the tooltip is being shown in.
     * @param player The player the tooltip is being shown to.
     * @param index The index of the slot the item is in.
     * @param shift Whether shift is held, which shows the exact figures rather than rounded ones.
     * @param advanced Whether the advanced tooltip setting is on.
     * @return The tooltip lines, or `null` where the slot is not watched or the item holds no energy.
     * @since 0.8.0
     */
    fun getTooltipFromContainerItem(stack: ItemStack, level: ClientLevel, player: Player, index: Int, shift: Boolean, advanced: Boolean): List<Component>? {
        if (index !in slots) return null
        val arrayIndex = slots.indexOf(index)

        val energy = stack.get(ToBaseMod.itemComponents.energy_data) ?: return null

        val new = stack.copy()
        new.remove(ToBaseMod.itemComponents.energy_data)

        val change = lastEnergyChange[arrayIndex]
        val info = ToBaseTooltipEngine.getEnergyInfo(energy.energy, energy.max, energy.maxInput, energy.maxOutput, change.coerceAtLeast(0L), null, -change.coerceAtMost(0L), null, advanced = shift)
        return TooltipEngine.injectItemTooltip(new, info, level, player, advanced)
    }

    override fun dataChanged(menu: AbstractContainerMenu, data: Int, value: Int) = Unit

}
