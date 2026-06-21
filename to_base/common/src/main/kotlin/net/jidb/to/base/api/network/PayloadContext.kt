package net.jidb.to.base.api.network

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

open class PayloadContext(open val player: Player) {
    open val level: Level get() = player.level()
}
