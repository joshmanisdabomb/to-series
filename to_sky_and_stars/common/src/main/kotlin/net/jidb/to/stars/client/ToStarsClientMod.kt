package net.jidb.to.stars.client

import net.jidb.to.base.client.mod.ToClientMod
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.content.ToStarsClientParticleLibrary
import net.jidb.to.stars.client.content.ToStarsClientPayloadLibrary

object ToStarsClientMod : ToClientMod() {

    override val common get() = ToStarsMod

    override val payloadHandlers = ToStarsClientPayloadLibrary
    override val particles = ToStarsClientParticleLibrary

}