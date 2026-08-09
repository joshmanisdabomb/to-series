package net.jidb.to.stars.block.entity

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.KilnMenu
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.level.block.entity.FuelValues
import net.minecraft.world.level.block.state.BlockState

class KilnBlockEntity(pos: BlockPos, state: BlockState) : AbstractFurnaceBlockEntity(ToStarsMod.blockEntities.kiln, pos, state, RecipeType.SMELTING) {

    override fun getDefaultName() = blockState.block.name

    override fun createMenu(id: Int, inventory: Inventory) = KilnMenu(id, inventory, this, dataAccess)

    override fun getBurnDuration(fuel: FuelValues, stack: ItemStack) = super.getBurnDuration(fuel, stack).div(2)

}
