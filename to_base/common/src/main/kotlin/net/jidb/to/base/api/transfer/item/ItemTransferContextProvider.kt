package net.jidb.to.base.api.transfer.item

import net.jidb.to.base.api.transfer.TransferContextProvider
import net.minecraft.core.Direction
import net.minecraft.world.Container

/**
 * Abstract class for a [TransferContextProvider] that retrieves instances of [ItemTransferContext].
 * This is implemented by each modloader to provide access to their own item transfer context providing API methods.
 *
 * This class requires an additional abstract method [fromContainer] to get an [ItemTransferContext] from a [Container].
 *
 * @since 0.6.0
 */
abstract class ItemTransferContextProvider : TransferContextProvider<ItemTransferContext> {

    /**
     * Retrieves an instance of [ItemTransferContext] based on the given [Container] and optional [Direction] as the side to get the context from.
     *
     * @param container The [Container] from which the [ItemTransferContext] should be retrieved.
     * @param side An optional direction specifying the side of the container, which may influence the retrieval behavior.
     * @return An instance of [ItemTransferContext] if available for the given [Container] and [Direction], or `null` if no context can be provided.
     * @since 0.6.0
     */
    abstract fun fromContainer(container: Container, side: Direction?): ItemTransferContext?

}
