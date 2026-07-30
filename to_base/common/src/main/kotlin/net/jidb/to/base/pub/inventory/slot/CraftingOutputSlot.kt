package net.jidb.to.base.pub.inventory.slot

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.RecipeCraftingHolder
import net.minecraft.world.item.ItemStack
import kotlin.math.min

open class CraftingOutputSlot(container: Container, val player: Player, val inputSlots: IntArray, slot: Int, x: Int, y: Int) : OutputSlot(container, slot, x, y) {

    private var removeCount = 0

    override fun remove(amount: Int): ItemStack {
        if (this.hasItem()) {
            removeCount += min(amount, this.item.count)
        }
        return super.remove(amount)
    }

    override fun onQuickCraft(picked: ItemStack, count: Int) {
        removeCount += count
        checkTakeAchievements(picked)
    }

    override fun onSwapCraft(count: Int) {
        removeCount += count
    }

    override fun safeClone(player: Player): ItemStack {
        val result = super.safeClone(player)
        result.item.onCraftedBy(result, player)
        return result
    }

    override fun checkTakeAchievements(carried: ItemStack) {
        if (this.removeCount > 0) {
            carried.onCraftedBy(player, removeCount)
        }

        (container as? RecipeCraftingHolder)?.awardUsedRecipes(player, inputSlots.map { container.getItem(it) })
        removeCount = 0
    }

    override fun onTake(player: Player, carried: ItemStack) {
        checkTakeAchievements(carried)
    }

}