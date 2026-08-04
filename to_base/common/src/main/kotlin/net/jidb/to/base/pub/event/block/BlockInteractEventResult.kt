package net.jidb.to.base.pub.event.block

import net.jidb.to.base.api.event.EventResult
import net.minecraft.world.InteractionResult

/**
 * The result a handler of a block interaction event returns, pairing the vanilla [InteractionResult] with whether the remaining handlers should still run.
 * Returning an [InteractionResult] alone would not be enough, because a handler that wants to answer for the interaction has to stop the block itself from answering too.
 *
 * @property result The interaction result to give back to vanilla.
 * @property cancel Whether the handlers attached after this one are skipped. Defaults to `false`.
 * @since 0.8.0
 */
data class BlockInteractEventResult(val result: InteractionResult, val cancel: Boolean = false) : EventResult {

    override fun cancelAfter() = cancel

}
