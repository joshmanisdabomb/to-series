package net.jidb.to.stars.client

import net.jidb.to.base.client.pub.mod.ToClientMod
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.content.ToStarsBlockEntityRenderersLibrary
import net.jidb.to.stars.client.content.ToStarsClientParticleLibrary
import net.jidb.to.stars.client.content.ToStarsClientPayloadHandlerLibrary
import net.jidb.to.stars.client.content.ToStarsEntityRenderersLibrary
import net.jidb.to.stars.client.content.ToStarsItemTintsLibrary
import net.jidb.to.stars.client.content.ToStarsModelLayersLibrary
import net.jidb.to.stars.client.content.ToStarsScreenLibrary
import net.jidb.to.stars.client.content.ToStarsSpecialModelLibrary
import net.jidb.to.stars.client.event.ToStarsClientEventHandlerLibrary

/**
 * [ToClientMod] implementation for To Sky and Stars, which names every library its client-side content is registered through.
 */
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
