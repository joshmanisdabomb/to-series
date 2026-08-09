package net.jidb.to.stars.block

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.KilnBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AbstractFurnaceBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

class KilnBlock(properties: Properties) : AbstractFurnaceBlock(properties) {

    override fun codec() = codec

    override fun openContainer(level: Level, pos: BlockPos, player: Player) {
        val kiln = level.getBlockEntity(pos, ToStarsMod.blockEntities.kiln).getOrNull() ?: return
        player.openMenu(kiln)
        //player.awardStat(Stats.INTERACT_WITH_FURNACE)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = KilnBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>) = createFurnaceTicker(level, type, ToStarsMod.blockEntities.kiln)

    companion object {
        val codec = simpleCodec(::KilnBlock)
    }

}
