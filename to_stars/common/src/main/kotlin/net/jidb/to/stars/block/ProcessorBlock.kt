package net.jidb.to.stars.block

import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.ProcessorBlockEntity
import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.info.ProcessorType
import net.jidb.to.stars.inventory.menu.ProcessorMenu
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

/**
 * A machine that runs processor recipes, which is opened into an interface telling the client which recipes it can run.
 *
 * @property processor The kind of processor this is, which decides the recipes it will accept.
 * @property machine The tier the processor is built at.
 * @param properties The block's own properties.
 */
abstract class ProcessorBlock(val processor: ProcessorType, val machine: MachineTier, properties: Properties) : BaseEntityBlock(properties) {

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        if (!level.isClientSide) {
            val entity = level.getBlockEntity(pos) as? ProcessorBlockEntity ?: return InteractionResult.SUCCESS
            Services.platform.inventory.openExtendedMenu(player, ToStarsMod.menus.centrifuge, state.getMenuProvider(level, pos), ProcessorMenu.ProcessorMenuData(entity))
            //player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
        }

        return InteractionResult.SUCCESS
    }

}
