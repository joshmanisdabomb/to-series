package net.jidb.to.base.pub.event.block

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

data class BlockInteractEventContext(val player: Player?, val stack: ItemStack, val level: Level, val state: BlockState, val pos: BlockPos, val hand: InteractionHand)
