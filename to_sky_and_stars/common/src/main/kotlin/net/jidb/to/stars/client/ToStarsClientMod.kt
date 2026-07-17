package net.jidb.to.stars.client

import net.jidb.to.base.client.pub.mod.ToClientMod
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.content.*
import net.jidb.to.stars.client.event.ToStarsClientEventHandlerLibrary

object ToStarsClientMod : ToClientMod() {

    override val common get() = ToStarsMod

    override val payloadHandlers = ToStarsClientPayloadHandlerLibrary
    override val blockEntityRenderers = ToStarsBlockEntityRenderersLibrary
    override val eventHandlers = ToStarsClientEventHandlerLibrary
    override val entityRenderers = ToStarsEntityRenderersLibrary
    override val modelLayers = ToStarsModelLayersLibrary
    override val specialModels = ToStarsSpecialModelLibrary
    override val itemTints = ToStarsItemTintsLibrary
    override val particles = ToStarsClientParticleLibrary
    override val screens = ToStarsScreenLibrary
}