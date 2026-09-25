package net.jidb.to.stars.recipe.processor

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.state.BlockState

/**
 * What is offered to a processor recipe when it is asked whether it matches: the items in the machine and the machine itself, since a recipe can be particular about which machine and tier runs it.
 *
 * @property machine The state of the machine asking.
 * @property items What is in its input slots.
 */
data class ProcessorRecipeInput(val machine: BlockState, val items: List<ItemStack>) : RecipeInput {

    override fun getItem(index: Int) = items[index]

    override fun size() = items.size

}
