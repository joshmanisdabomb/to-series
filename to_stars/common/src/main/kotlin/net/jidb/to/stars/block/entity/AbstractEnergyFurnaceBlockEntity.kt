package net.jidb.to.stars.block.entity

import net.jidb.to.base.api.helper.KotlinHelper.either
import net.jidb.to.base.api.helper.RegistryHelper.resourceKey
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.stars.block.EnergyFurnaceBlock
import net.jidb.to.stars.info.EnergyFurnaceType
import net.jidb.to.stars.inventory.menu.EnergyFurnaceMenu
import net.jidb.to.stars.recipe.processor.ProcessorRecipeIngredient
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

abstract class AbstractEnergyFurnaceBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : AbstractProcessorBlockEntity<AbstractCookingRecipe>(type, pos, state) {

    override var inventory = NonNullList.withSize(EnergyFurnaceMenu.allSlots.size, ItemStack.EMPTY)
    override val inputSlots = EnergyFurnaceMenu.inputSlots
    override val outputSlots = EnergyFurnaceMenu.outputSlots
    override val batterySlots = EnergyFurnaceMenu.batterySlots

    abstract val quickCheck: RecipeManager.CachedCheck<SingleRecipeInput, out AbstractCookingRecipe>

    override val energy = ToEnergyBlockEntityHandler(
        (state.block as? EnergyFurnaceBlock)?.machine?.machineBuffer ?: 0L,
        (state.block as? EnergyFurnaceBlock)?.machine?.maxInput ?: 0L,
        (state.block as? EnergyFurnaceBlock)?.machine?.maxOutput ?: 0L,
    )

    override var machineSpeed = (state.block as EnergyFurnaceBlock).machine.machineSpeed

    override var machineUsage = (state.block as EnergyFurnaceBlock).machine.machineUsage

    override var machineBonusMax = (state.block as EnergyFurnaceBlock).machine.machineBonusMax

    override var machineBonus = (state.block as EnergyFurnaceBlock).machine.machineBonus

    override fun createMenu(id: Int, player: Inventory) = EnergyFurnaceMenu(id, player, this, dataAccess)

    override fun getRecipe(level: ServerLevel): RecipeHolder<out AbstractCookingRecipe>? {
        val input = SingleRecipeInput(getItem(inputSlots.first()))
        return quickCheck.getRecipeFor(input, level).getOrNull()
    }

    override fun getRecipeInputs(recipe: RecipeHolder<out AbstractCookingRecipe>) = listOf(ProcessorRecipeIngredient(recipe.value.input(), 1))

    override fun getRecipeOutputs(recipe: RecipeHolder<out AbstractCookingRecipe>, level: ServerLevel) = listOf(recipe.value.assemble(SingleRecipeInput(getItem(inputSlots.first()))))

    override fun getRecipeEnergy(recipe: RecipeHolder<out AbstractCookingRecipe>) = 1000L * recipe.value.cookingTime() * ((blockState.block as EnergyFurnaceBlock).furnace == EnergyFurnaceType.FURNACE).either(1, 2)

    override fun getRecipeTime(recipe: RecipeHolder<out AbstractCookingRecipe>) = recipe.value.cookingTime()

    override fun getRecipeIcon(recipe: RecipeHolder<out AbstractCookingRecipe>) = recipe.value.assemble(SingleRecipeInput(getItem(inputSlots.first()))).item.resourceKey

    companion object {

        /**
         * Ticks the block entity.
         *
         * @param level The level it is in.
         * @param pos Its position.
         * @param state Its state.
         * @param entity The block entity being ticked.
         */
        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: AbstractEnergyFurnaceBlockEntity) {
            AbstractProcessorBlockEntity.tick(level, pos, state, entity)
        }

    }

}
