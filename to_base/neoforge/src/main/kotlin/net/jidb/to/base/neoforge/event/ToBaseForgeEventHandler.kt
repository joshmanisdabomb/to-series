package net.jidb.to.base.neoforge.event

import net.jidb.to.base.hooks.event.ToBaseEventLibrary
import net.jidb.to.base.hooks.event.advancements.AdvancementEventContext
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.AdvancementEvent

object ToBaseForgeEventHandler {

    @SubscribeEvent
    fun onAdvancementEarnEvent(event: AdvancementEvent.AdvancementEarnEvent) {
        ToBaseEventLibrary.advancement_grant_post.call(AdvancementEventContext(event.entity as ServerPlayer, event.advancement))
    }

}
