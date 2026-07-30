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

class ToEnergyItemContainerListener(val slots: IntArray, val level: () -> Level?) : ContainerListener {

    private val lastEnergySeen = LongArray(slots.size)
    private val lastEnergyChange = LongArray(slots.size)
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