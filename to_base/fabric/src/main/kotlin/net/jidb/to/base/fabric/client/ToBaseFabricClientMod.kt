package net.jidb.to.base.fabric.client

import net.jidb.to.base.client.ToBaseClientMod
import net.jidb.to.base.fabric.client.event.ToBaseFabricClientEventHandlerLibrary
import net.jidb.to.base.fabric.client.mod.ToFabricClientMod

object ToBaseFabricClientMod : ToFabricClientMod() {

    override val client get() = ToBaseClientMod

    override val events = ToBaseFabricClientEventHandlerLibrary

}
