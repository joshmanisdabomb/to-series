package net.jidb.to.base.pub.event.block

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

/**
 * An [Event] that fires when a player uses an item on a block, before the block itself is given the chance to respond.
 * A handler answers with a [BlockInteractEventResult], which can both decide the interaction and stop the handlers after it from running.
 *
 * @param id A unique [Identifier] for the event.
 * @since 0.8.0
 */
class UseItemOnBlockEvent(id: Identifier) : Event<BlockInteractEventContext, BlockInteractEventResult>(id)
