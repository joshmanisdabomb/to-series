package net.jidb.to.stars.fabric

import net.jidb.to.base.fabric.mod.ToFabricMod
import net.jidb.to.stars.ToStarsMod

/**
 * [ToFabricMod] implementation for the content mod, i.e. the entry point Fabric loads it through.
 */
object ToStarsFabricMod : ToFabricMod() {

    override val common get() = ToStarsMod

}
