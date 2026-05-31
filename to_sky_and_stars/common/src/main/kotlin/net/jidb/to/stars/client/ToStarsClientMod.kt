package net.jidb.to.stars.client

import net.jidb.to.base.client.mod.ToClientMod
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.content.ToStarsClientParticleLibrary
import net.jidb.to.stars.client.content.ToStarsClientPayloadHandlerLibrary
import net.jidb.to.stars.client.content.ToStarsEntityRenderersLibrary
import net.jidb.to.stars.client.content.ToStarsScreenLibrary

object ToStarsClientMod : ToClientMod() {

    override val common get() = ToStarsMod

    override val payloadHandlers = ToStarsClientPayloadHandlerLibrary
    override val entityRenderers = ToStarsEntityRenderersLibrary
    override val particles = ToStarsClientParticleLibrary
    override val screens = ToStarsScreenLibrary

}