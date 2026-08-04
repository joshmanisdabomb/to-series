package net.jidb.to.base.pub.inventory.slot

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.RecipeCraftingHolder
import net.minecraft.world.item.ItemStack
import kotlin.math.min

/**
 * An [OutputSlot] holding the result of a recipe, which awards the crafting statistics and recipe unlocks that taking one entails.
 * Every way of taking from the slot feeds the same running count, because vanilla splits a single "how many were crafted" question across a quick-move, a hotbar swap and an ordinary pickup.
 *
 * @param container The container the slot reads from.
 * @property player The player crafting, who the statistics and recipe unlocks are awarded to.
 * @property inputSlots The indices of the slots the recipe consumed from, which the unlocks are worked out against.
 * @param slot The index of the slot within the container.
 * @param x The x position of the slot in the screen.
 * @param y The y position of the slot in the screen.
 * @since 0.8.0
 */
open class CraftingOutputSlot(container: Container, val player: Player, val inputSlots: IntArray, slot: Int, x: Int, y: Int) : OutputSlot(container, slot, x, y) {

    /**
     * How many items have been taken since the statistics were last awarded, reset each time they are.
     *
     * @since 0.8.0
     */
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
