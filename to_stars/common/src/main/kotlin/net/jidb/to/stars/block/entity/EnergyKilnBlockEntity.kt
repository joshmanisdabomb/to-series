package net.jidb.to.stars.block.entity

import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class EnergyKilnBlockEntity(pos: BlockPos, state: BlockState) : AbstractEnergyFurnaceBlockEntity(ToStarsMod.blockEntities.energy_kiln, pos, state) {

    override val quickCheck = KilnBlockEntity.createCustomCheck()

}
