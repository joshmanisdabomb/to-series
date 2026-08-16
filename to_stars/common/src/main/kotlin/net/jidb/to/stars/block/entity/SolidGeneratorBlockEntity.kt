package net.jidb.to.stars.block.entity

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.SolidGeneratorMenu
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class SolidGeneratorBlockEntity(pos: BlockPos, state: BlockState) : HeatGeneratorBlockEntity(ToStarsMod.blockEntities.solid_generator, pos, state) {

    override fun createMenu(id: Int, player: Inventory) = SolidGeneratorMenu(id, player, this, dataAccess)

    override fun getFuelValue(stack: ItemStack): Float {
        return SolidGeneratorMenu.getFuelValue(stack, level ?: return 0f)
    }

    override fun getFuelDuration(stack: ItemStack): Short {
        return SolidGeneratorMenu.getFuelDuration(stack, level ?: return 0)
    }

}
