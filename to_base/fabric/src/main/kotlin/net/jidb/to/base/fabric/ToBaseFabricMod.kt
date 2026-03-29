package net.jidb.to.base.fabric

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.fabric.mod.ToFabricMod

object ToBaseFabricMod : ToFabricMod() {
	override val common get() = ToBaseMod
}