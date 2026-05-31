package net.jidb.to.base.hooks.event.advancements

import net.minecraft.advancements.AdvancementHolder
import net.minecraft.server.level.ServerPlayer

data class AdvancementEventContext(val player: ServerPlayer, val advancement: AdvancementHolder)