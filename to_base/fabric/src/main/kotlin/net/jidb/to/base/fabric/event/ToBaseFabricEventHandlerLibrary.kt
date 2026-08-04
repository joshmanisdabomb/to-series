package net.jidb.to.base.fabric.event

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.BlockEvents
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.fabric.library.FabricEventHandlerLibrary
import net.jidb.to.base.pub.event.ToBaseEventLibrary
import net.jidb.to.base.pub.event.block.BlockInteractEventContext
import net.jidb.to.base.pub.event.item.ModifyItemComponentEventContext
import net.jidb.to.base.pub.event.level.ServerLevelEventContext
import net.minecraft.world.InteractionResult

/**
 * [FabricEventHandlerLibrary] implementation holding the callbacks that raise the base mod's own cross-loader events from Fabric's.
 *
 * @since 0.6.0
 */
object ToBaseFabricEventHandlerLibrary : FabricEventHandlerLibrary(ToBaseMod.modid) {

    /**
     * Raises [net.jidb.to.base.pub.event.ToBaseEventLibrary.server_level_tick_pre] from Fabric's own start-of-level-tick event.
     *
     * @since 0.6.0
     */
    val server_level_tick_pre by this(ServerTickEvents.START_LEVEL_TICK, ServerTickEvents.StartLevelTick { level ->
        ToBaseEventLibrary.server_level_tick_pre.call(ServerLevelEventContext(level))
    })

    /**
     * Raises [net.jidb.to.base.pub.event.ToBaseEventLibrary.modify_default_components] from Fabric's own default component event.
     *
     * @since 0.6.0
     */
    val modify_default_components by this(DefaultItemComponentEvents.MODIFY, DefaultItemComponentEvents.ModifyCallback {
        ToBaseEventLibrary.modify_default_components.call(ModifyItemComponentEventContext({ item, patch ->
            it.modify(item.asItem()) { builder, holder, item -> patch(builder, item) }
        }, { predicate, patch ->
            it.modify(predicate) { builder, holder, item -> patch(builder, item) }
        }))
    })

    /**
     * Raises [net.jidb.to.base.pub.event.ToBaseEventLibrary.use_item_on_block] from Fabric's own block use event, answering it with whatever the last handler decided.
     *
     * @since 0.8.0
     */
    val use_item_on_block by this(BlockEvents.USE_ITEM_ON, BlockEvents.UseItemOnCallback { stack, state, level, pos, player, hand, result ->
        ToBaseEventLibrary.use_item_on_block.call(BlockInteractEventContext(player, stack, level, state, pos, hand)).results.lastOrNull()?.result ?: InteractionResult.TRY_WITH_EMPTY_HAND
    })

}
