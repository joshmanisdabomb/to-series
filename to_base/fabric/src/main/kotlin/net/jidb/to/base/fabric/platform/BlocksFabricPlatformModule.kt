package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.jidb.to.base.api.platform.BlocksPlatformModule
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

/**
 * [BlocksPlatformModule] implementation for Fabric.
 *
 * @since 0.1.0
 */
object BlocksFabricPlatformModule : BlocksPlatformModule() {

    override fun <T : BlockEntity> createBlockEntityType(factory: (pos: BlockPos, state: BlockState) -> T, vararg blocks: Block, opNbt: Boolean) = FabricBlockEntityTypeBuilder.create(factory, *blocks)
        .canPotentiallyExecuteCommands(opNbt)
        .build()

}
