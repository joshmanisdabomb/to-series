package net.jidb.to.base.neoforge.event

import net.jidb.to.base.pub.event.ToBaseEventLibrary
import net.jidb.to.base.pub.event.advancements.AdvancementEventContext
import net.jidb.to.base.pub.event.block.BlockInteractEventContext
import net.jidb.to.base.pub.event.entity.EntityEventContext
import net.jidb.to.base.pub.event.level.ServerLevelEventContext
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.AdvancementEvent
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
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

    @SubscribeEvent
    fun onEntityPreTickEvent(event: EntityTickEvent.Pre) {
        if (!event.entity.level().isClientSide) {
            ToBaseEventLibrary.server_entity_tick_pre.call(EntityEventContext(event.entity))
        }
    }

    @SubscribeEvent
    fun onUseItemOnBlockEvent(event: UseItemOnBlockEvent) {
        val state = event.level.getBlockState(event.pos)
        val results = ToBaseEventLibrary.use_item_on_block.call(BlockInteractEventContext(event.player, event.itemStack, event.level, state, event.pos, event.hand))
        if (results.cancelled) {
            event.cancelWithResult(results.results.last().result)
        }
    }

}
