package net.jidb.to.base.fabric.client

import net.jidb.to.base.client.ToBaseClientMod
import net.jidb.to.base.fabric.client.event.ToBaseFabricClientEventHandlerLibrary
import net.jidb.to.base.fabric.client.mod.ToFabricClientMod

/**
 * [ToFabricClientMod] implementation for the base mod itself, i.e. the client-side entry point Fabric loads it through.
 *
 * @since 0.0.1
 */
object ToBaseFabricClientMod : ToFabricClientMod() {

    override val client get() = ToBaseClientMod

    override val events = ToBaseFabricClientEventHandlerLibrary

}
