package net.jidb.to.base.pub.event.block

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * The context given to a handler of a block interaction event, naming the block interacted with and what was used on it.
 *
 * @property player The player interacting with the block, or `null` where the interaction has no player behind it.
 * @property stack The item stack being used on the block.
 * @property level The level the block is in.
 * @property state The state of the block being interacted with.
 * @property pos The position of the block being interacted with.
 * @property hand The hand the item is being used from.
 * @since 0.8.0
 */
data class BlockInteractEventContext(val player: Player?, val stack: ItemStack, val level: Level, val state: BlockState, val pos: BlockPos, val hand: InteractionHand)
