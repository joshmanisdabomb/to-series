package net.jidb.to.base.pub.event

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.pub.event.advancements.AdvancementGrantPostEvent
import net.jidb.to.base.pub.event.block.UseItemOnBlockEvent
import net.jidb.to.base.pub.event.entity.ServerEntityTickPreEvent
import net.jidb.to.base.pub.event.item.ModifyDefaultItemComponentsEvent
import net.jidb.to.base.pub.event.level.ServerLevelTickPreEvent

object ToBaseEventLibrary : SimpleLibrary<Event<*, *>>(ToBaseMod.modid) {

    val advancement_grant_post by this { AdvancementGrantPostEvent(it.id) }
    val server_level_tick_pre by this { ServerLevelTickPreEvent(it.id) }
    val server_entity_tick_pre by this { ServerEntityTickPreEvent(it.id) }
    val modify_default_components by this { ModifyDefaultItemComponentsEvent(it.id) }
    val use_item_on_block by this { UseItemOnBlockEvent(it.id) }

}
