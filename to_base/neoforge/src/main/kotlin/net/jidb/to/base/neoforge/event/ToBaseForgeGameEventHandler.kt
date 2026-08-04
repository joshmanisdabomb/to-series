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

/**
 * Holds the listeners that raise the base mod's own cross-loader events from the Neoforge events on the game bus.
 *
 * @since 0.6.0
 */
object ToBaseForgeGameEventHandler {

    /**
     * Raises [ToBaseEventLibrary.advancement_grant_post] when a player earns an advancement.
     *
     * @param event The Neoforge event.
     * @since 0.6.0
     */
    @SubscribeEvent
    fun onAdvancementEarnEvent(event: AdvancementEvent.AdvancementEarnEvent) {
        ToBaseEventLibrary.advancement_grant_post.call(AdvancementEventContext(event.entity as ServerPlayer, event.advancement))
    }

    /**
     * Raises [ToBaseEventLibrary.server_level_tick_pre] at the start of a server level's tick.
     *
     * @param event The Neoforge event.
     * @since 0.6.0
     */
    @SubscribeEvent
    fun onLevelPreTickEvent(event: LevelTickEvent.Pre) {
        val level = event.level
        if (level.isClientSide || level !is ServerLevel) return
        ToBaseEventLibrary.server_level_tick_pre.call(ServerLevelEventContext(level))
    }

    /**
     * Raises [ToBaseEventLibrary.server_entity_tick_pre] at the start of an entity's tick on the server.
     *
     * @param event The Neoforge event.
     * @since 0.6.0
     */
    @SubscribeEvent
    fun onEntityPreTickEvent(event: EntityTickEvent.Pre) {
        if (!event.entity.level().isClientSide) {
            ToBaseEventLibrary.server_entity_tick_pre.call(EntityEventContext(event.entity))
        }
    }

    /**
     * Raises [ToBaseEventLibrary.use_item_on_block] when an item is used on a block, cancelling the interaction with whatever the last handler decided where any of them answered.
     *
     * @param event The Neoforge event.
     * @since 0.8.0
     */
    @SubscribeEvent
    fun onUseItemOnBlockEvent(event: UseItemOnBlockEvent) {
        val state = event.level.getBlockState(event.pos)
        val results = ToBaseEventLibrary.use_item_on_block.call(BlockInteractEventContext(event.player, event.itemStack, event.level, state, event.pos, event.hand))
        if (results.cancelled) {
            event.cancelWithResult(results.results.last().result)
        }
    }

}
