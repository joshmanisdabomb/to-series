package net.jidb.to.base.hooks.event

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.event.Event
import net.jidb.to.base.hooks.event.advancements.AdvancementGrantPostEvent
import net.jidb.to.base.library.SimpleLibrary

object ToBaseEventLibrary : SimpleLibrary<Event<*, *>>(ToBaseMod.modid) {

    val advancement_grant_post by this { AdvancementGrantPostEvent(it.id) }

}