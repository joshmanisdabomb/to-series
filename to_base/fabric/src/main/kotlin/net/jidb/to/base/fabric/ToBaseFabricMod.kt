package net.jidb.to.base.fabric

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.fabric.event.ToBaseFabricEventHandlerLibrary
import net.jidb.to.base.fabric.mod.ToFabricMod

/**
 * [ToFabricMod] implementation for the base mod itself, i.e. the entry point Fabric loads it through.
 *
 * @since 0.0.1
 */
object ToBaseFabricMod : ToFabricMod() {

    override val common get() = ToBaseMod

    override val events = ToBaseFabricEventHandlerLibrary

}
