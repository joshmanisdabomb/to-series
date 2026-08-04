package net.jidb.to.base.pub.event.advancements

import net.minecraft.advancements.AdvancementHolder
import net.minecraft.server.level.ServerPlayer

/**
 * The context given to a handler of an advancement event, naming the advancement and the player it concerns.
 *
 * @property player The player the advancement was granted to.
 * @property advancement The advancement that was granted.
 * @since 0.5.0
 */
data class AdvancementEventContext(val player: ServerPlayer, val advancement: AdvancementHolder)
