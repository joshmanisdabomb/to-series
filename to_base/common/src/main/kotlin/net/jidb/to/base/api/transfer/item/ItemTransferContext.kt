package net.jidb.to.base.api.transfer.item

import net.jidb.to.base.api.transfer.TransferContext

/**
 * Represents a cross-platform transfer context for handling operations related to item-type resources.
 * This is implemented by each modloader to provide access to their own item transfer API methods.
 *
 * @since 0.6.0
 */
interface ItemTransferContext : TransferContext<ItemResource>
