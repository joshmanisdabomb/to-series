package net.jidb.to.stars.fabric.client

import net.jidb.to.base.fabric.client.mod.ToFabricClientMod
import net.jidb.to.stars.client.ToStarsClientMod

object ToStarsFabricClientMod : ToFabricClientMod() {

    override val client get() = ToStarsClientMod

}
