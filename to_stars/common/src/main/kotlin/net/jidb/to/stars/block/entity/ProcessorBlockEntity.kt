package net.jidb.to.stars.block.entity

import net.jidb.to.base.api.helper.RegistryHelper.resourceKey
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.ProcessorBlock
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.jidb.to.stars.recipe.processor.ProcessorRecipeInput
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

/**
 * The block entity of a machine that processes processor recipes.
 *
 * @param type The block entity type being built.
 * @param pos The position of the block.
 * @param state The state of the block.
 */
abstract class ProcessorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : AbstractProcessorBlockEntity<ProcessorRecipe>(type, pos, state) {

    /**
     * The cached lookup that finds which recipe the current ingredients match, so that every recipe need not be tried each tick.
     */
    private val quickCheck = RecipeManager.createCheck(ToStarsMod.recipeTypes.processor)

    override val energy = ToEnergyBlockEntityHandler(
        (state.block as? ProcessorBlock)?.machine?.machineBuffer ?: 0L,
        (state.block as? ProcessorBlock)?.machine?.maxInput ?: 0L,
        (state.block as? ProcessorBlock)?.machine?.maxOutput ?: 0L,
    )

    override var machineSpeed = (state.block as ProcessorBlock).machine.machineSpeed

    override var machineUsage = (state.block as ProcessorBlock).machine.machineUsage

    override var machineBonusMax = (state.block as ProcessorBlock).machine.machineBonusMax

    override var machineBonus = (state.block as ProcessorBlock).machine.machineBonus

    override fun getRecipe(level: ServerLevel): RecipeHolder<out ProcessorRecipe>? {
        val input = ProcessorRecipeInput(blockState, inputSlots.map(::getItem))
        return quickCheck.getRecipeFor(input, level).getOrNull()
    }

    override fun getRecipeInputs(recipe: RecipeHolder<out ProcessorRecipe>) = recipe.value.ingredients

    override fun getRecipeOutputs(recipe: RecipeHolder<out ProcessorRecipe>, level: ServerLevel) = recipe.value.assembleAll(recipe.id.identifier(), this, level, this.seed!!)

    override fun getRecipeEnergy(recipe: RecipeHolder<out ProcessorRecipe>) = recipe.value.energy

    override fun getRecipeTime(recipe: RecipeHolder<out ProcessorRecipe>) = recipe.value.time

    override fun getRecipeIcon(recipe: RecipeHolder<out ProcessorRecipe>) = recipe.value.getFirstOutput().item.resourceKey

}
