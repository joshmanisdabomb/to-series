package net.jidb.to.stars.recipe.processor

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.state.BlockState

data class ProcessorRecipeInput(val machine: BlockState, val items: List<ItemStack>) : RecipeInput {

    override fun getItem(index: Int) = items[index]

    override fun size() = items.size

}