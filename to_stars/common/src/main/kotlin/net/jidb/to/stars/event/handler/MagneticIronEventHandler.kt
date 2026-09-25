package net.jidb.to.stars.event.handler

import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.pub.event.block.BlockInteractEventContext
import net.jidb.to.base.pub.event.block.BlockInteractEventResult
import net.jidb.to.stars.ToStarsMod
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks

/**
 * Turns an iron ingot into magnetic iron when it is used on a lodestone.
 */
class MagneticIronEventHandler : EventHandler<BlockInteractEventContext, BlockInteractEventResult>() {

    override fun invoke(context: BlockInteractEventContext): BlockInteractEventResult {
        val player = context.player
        if (player != null && context.stack.`is`(Items.IRON_INGOT) && context.state.`is`(Blocks.LODESTONE)) {
            context.stack.consume(1, player)
            if (!context.level.isClientSide) {
                player.drop(ItemStack(ToStarsMod.items.magnetic_iron), false, true)
            }
            return BlockInteractEventResult(InteractionResult.SUCCESS, true)
        }
        return BlockInteractEventResult(InteractionResult.TRY_WITH_EMPTY_HAND)
    }

}
