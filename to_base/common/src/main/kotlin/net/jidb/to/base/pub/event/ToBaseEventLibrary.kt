package net.jidb.to.base.pub.event

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.pub.event.advancements.AdvancementGrantPostEvent
import net.jidb.to.base.pub.event.block.UseItemOnBlockEvent
import net.jidb.to.base.pub.event.entity.ServerEntityTickPreEvent
import net.jidb.to.base.pub.event.item.ModifyDefaultItemComponentsEvent
import net.jidb.to.base.pub.event.level.ServerLevelTickPreEvent

/**
 * [SimpleLibrary] implementation that declares the cross-platform events To Lay the Foundations fires, and provides access to them in one place.
 * Each entry is the single instance of its event, which a mod attaches a handler to with [Event.attach], usually through a [net.jidb.to.base.pub.library.EventHandlerLibrary].
 *
 * The loader projects are what actually fire these, translating a Fabric callback or a Forge event into a call on the entry here.
 * See [net.jidb.to.base.client.pub.event.ToBaseClientEventLibrary] for the events that only fire on the client.
 *
 * @since 0.5.0
 */
object ToBaseEventLibrary : SimpleLibrary<Event<*, *>>(ToBaseMod.modid) {

    /**
     * Fires after a player has been granted an advancement.
     *
     * @since 0.5.0
     */
    val advancement_grant_post by this { AdvancementGrantPostEvent(it.id) }

    /**
     * Fires before a server level ticks.
     *
     * @since 0.6.0
     */
    val server_level_tick_pre by this { ServerLevelTickPreEvent(it.id) }

    /**
     * Fires before an entity ticks on the server.
     *
     * @since 0.8.0
     */
    val server_entity_tick_pre by this { ServerEntityTickPreEvent(it.id) }

    /**
     * Fires while the default data components of every item are being built, so that a handler can patch the components of items it does not own.
     *
     * @since 0.6.0
     */
    val modify_default_components by this { ModifyDefaultItemComponentsEvent(it.id) }

    /**
     * Fires when a player uses an item on a block, before the block itself is given the chance to respond.
     *
     * @since 0.8.0
     */
    val use_item_on_block by this { UseItemOnBlockEvent(it.id) }

}
