package net.jidb.to.base.neoforge.event

import net.jidb.to.base.pub.event.ToBaseEventLibrary
import net.jidb.to.base.pub.event.advancements.AdvancementEventContext
import net.jidb.to.base.pub.event.level.ServerLevelEventContext
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.AdvancementEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent

object ToBaseForgeGameEventHandler {

    @SubscribeEvent
    fun onAdvancementEarnEvent(event: AdvancementEvent.AdvancementEarnEvent) {
        ToBaseEventLibrary.advancement_grant_post.call(AdvancementEventContext(event.entity as ServerPlayer, event.advancement))
    }

    @SubscribeEvent
    fun onLevelPreTickEvent(event: LevelTickEvent.Pre) {
        val level = event.level
        if (level.isClientSide || level !is ServerLevel) return
        ToBaseEventLibrary.server_level_tick_pre.call(ServerLevelEventContext(level))
    }

}
