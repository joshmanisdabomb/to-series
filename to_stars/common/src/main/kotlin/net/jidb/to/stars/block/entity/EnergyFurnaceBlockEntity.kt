package net.jidb.to.stars.block.entity

import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.BlockPos
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState

class EnergyFurnaceBlockEntity(pos: BlockPos, state: BlockState) : AbstractEnergyFurnaceBlockEntity(ToStarsMod.blockEntities.energy_furnace, pos, state) {

    override val quickCheck = RecipeManager.createCheck(RecipeType.SMELTING)

}
