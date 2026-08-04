package net.jidb.to.stars.fabric.client

import net.jidb.to.base.fabric.client.mod.ToFabricClientMod
import net.jidb.to.stars.client.ToStarsClientMod

/**
 * [ToFabricClientMod] implementation for the content mod, i.e. the client-side entry point Fabric loads it through.
 */
object ToStarsFabricClientMod : ToFabricClientMod() {

    override val client get() = ToStarsClientMod

}
