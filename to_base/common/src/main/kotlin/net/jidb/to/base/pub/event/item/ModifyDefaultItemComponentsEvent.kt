package net.jidb.to.base.pub.event.item

import net.jidb.to.base.api.event.Event
import net.minecraft.resources.Identifier

/**
 * An [Event] that fires while the default data components of every item are being built.
 * This is how a mod adds a component to an item it does not own, such as giving a vanilla item one of its own, which is otherwise only possible at the point the item is registered.
 *
 * @param id A unique [Identifier] for the event.
 * @since 0.6.0
 */
class ModifyDefaultItemComponentsEvent(id: Identifier) : Event<ModifyItemComponentEventContext, Unit>(id)
