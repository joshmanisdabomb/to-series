package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.BlocksPlatformModule
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

object BlocksForgePlatformModule : BlocksPlatformModule() {

    override fun <T : BlockEntity> createBlockEntityType(factory: (pos: BlockPos, state: BlockState) -> T, vararg blocks: Block, opNbt: Boolean) = BlockEntityType(factory, opNbt, *blocks)

}
