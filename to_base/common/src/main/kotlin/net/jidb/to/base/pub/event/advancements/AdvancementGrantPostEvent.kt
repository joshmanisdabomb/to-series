package net.jidb.to.base.pub.event.advancements

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

/**
 * An [Event] that fires after a player has been granted an advancement.
 * Handlers cannot undo the grant, only respond to it, so no result is returned.
 *
 * @param id A unique [Identifier] for the event.
 * @since 0.5.0
 */
class AdvancementGrantPostEvent(id: Identifier) : Event<AdvancementEventContext, Unit>(id)
