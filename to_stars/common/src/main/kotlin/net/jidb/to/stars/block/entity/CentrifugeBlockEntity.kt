package net.jidb.to.stars.block.entity

import net.jidb.to.base.api.helper.KotlinHelper.transpose
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.CentrifugeMenu
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT
import kotlin.jvm.optionals.getOrNull

/**
 * The block entity of a centrifuge, which separates uranium into its enriched and heavy forms.
 *
 * A centrifuge's outputs are not interchangeable: each output slot only accepts what the recipe's preview says belongs there, so the heavy and enriched results always land in the same places.
 *
 * @param pos The position of the block.
 * @param state The state of the block.
 */
class CentrifugeBlockEntity(pos: BlockPos, state: BlockState) : ProcessorBlockEntity(ToStarsMod.blockEntities.centrifuge, pos, state) {

    override var inventory = NonNullList.withSize(CentrifugeMenu.allSlots.size, ItemStack.EMPTY)
    override val inputSlots = CentrifugeMenu.inputSlots
    override val outputSlots = CentrifugeMenu.outputSlots
    override val batterySlots = CentrifugeMenu.batterySlots

    override fun createMenu(id: Int, player: Inventory) = CentrifugeMenu(id, player, this, dataAccess)

    override fun outputResults(recipe: RecipeHolder<out ProcessorRecipe>, results: List<ItemStack>): Boolean {
        val preview = recipe.value.preview
        val slots = outputSlots.map(::getSlot)
        val items = preview.transpose().map { it.mapNotNull { it.getOrNull()?.item?.value() }.distinct() }
        println(preview)
        println(preview.transpose())
        println(items)

        val stacks = slots.mapNotNull { it?.get()?.copy() }.toMutableList()
        for (stack in results) {
            if (stack.isEmpty) continue
            var remaining = stack.count

            for ((index, existing) in stacks.withIndex()) {
                if (remaining == 0) break
                if (existing.isEmpty || !ItemStack.isSameItemSameComponents(existing, stack)) continue
                if (stack.item !in items[index]) continue

                val amount = minOf(remaining, existing.maxStackSize - existing.count)
                existing.grow(amount)
                remaining -= amount
            }

            for (index in stacks.indices) {
                if (remaining == 0) break
                if (!stacks[index].isEmpty) continue
                if (stack.item !in items[index]) continue

                val amount = minOf(remaining, stack.maxStackSize)
                stacks[index] = stack.copyWithCount(amount)
                remaining -= amount
            }

            if (remaining > 0) return false
        }

        stacks.forEachIndexed { index, stack -> slots[index]?.set(stack) }
        return true
    }

    companion object {

        /**
         * Ticks the block entity.
         *
         * @param level The level it is in.
         * @param pos Its position.
         * @param state Its state.
         * @param entity The block entity being ticked.
         */
        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: CentrifugeBlockEntity) {
            ProcessorBlockEntity.tick(level, pos, state, entity)

            if (level.isClientSide) {
                if (state.getValue(LIT)) {
                    entity.progress += 1
                } else {
                    entity.progress = 0
                }
            }
        }

    }

}
