package net.jidb.to.base.api.transfer

import net.jidb.to.base.api.transfer.item.ItemTransferContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * An interface for getting [TransferContext]s from different sources, such as directionally from [Block] and [net.minecraft.world.level.block.entity.BlockEntity], or from an [ItemStack].
 *
 * You can add these to [net.jidb.to.base.api.mod.ToContentMod.transferProviders] to get contexts with them later.
 *
 * @param C The type of [TransferContext] to retrieve.
 * @since 0.6.0
 */
interface TransferContextProvider<C : TransferContext<*>> {

    /**
     * Retrieves a [TransferContext] of type [C] at the given [Level] and [BlockPos], using an optional [Direction] as the side to get the context from.
     * An implementation may consider both a [net.minecraft.world.level.block.Block] and a [net.minecraft.world.level.block.entity.BlockEntity] as potential transfer context providers.
     *
     * @param level The [Level] where the block is located.
     * @param pos The position of the block within the level.
     * @param side The side of the block to consider, or null if side-specific behavior is not required.
     * @return The transfer context of type [C], or null if no context could be retrieved.
     * @since 0.6.0
     */
    fun fromBlock(level: Level, pos: BlockPos, side: Direction? = null): C?

    /**
     * Retrieves a [TransferContext] of type [C] associated with the given [ItemStack].
     * An implementation can use the provided [ItemStack] and context to create the transfer context, optionally considering the slot the queried [stack] resides in.
     *
     * @param stack The [ItemStack] to retrieve the transfer context from.
     * @param context The [ItemTransferContext] holding the [stack].
     * @param slot The specific slot the [stack] is in.
     * @return The transfer context of type [C], or null if no context could be retrieved.
     * @since 0.6.0
     */
    fun fromItemStack(stack: ItemStack, context: ItemTransferContext, slot: Int): C?

}
