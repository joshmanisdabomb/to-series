package net.jidb.to.stars.fabric

import net.jidb.to.base.fabric.mod.ToFabricMod
import net.jidb.to.stars.ToStarsMod

object ToStarsFabricMod : ToFabricMod() {
	override val common get() = ToStarsMod
}