package net.jidb.to.base.pub.event.block

import net.jidb.to.base.api.event.EventResult
import net.minecraft.world.InteractionResult

data class BlockInteractEventResult(val result: InteractionResult, val cancel: Boolean = false) : EventResult {

    override fun cancelAfter() = cancel

}