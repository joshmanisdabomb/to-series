package net.jidb.to.stars

import net.jidb.to.base.mod.ToMod
import net.jidb.to.stars.content.*
import net.jidb.to.stars.content.key.ToStarsConfiguredFeatureLibrary
import net.jidb.to.stars.content.key.ToStarsPlacedFeatureLibrary

object ToStarsMod : ToMod() {
    const val MOD_ID = "to_sky_and_stars"
    override val modid = MOD_ID

    override val blocks = ToStarsBlockLibrary
    override val items = ToStarsItemLibrary
    override val blockItems = ToStarsBlockItemLibrary
    override val tabs = ToStarsCreativeTabLibrary

    override val blockEntities = ToStarsBlockEntityLibrary
    override val menus = ToStarsMenuLibrary

    override val entities = ToStarsEntityLibrary

    override val payloads = ToStarsPayloadLibrary
    override val payloadHandlers = ToStarsPayloadHandlerLibrary

    override val blockTags = ToStarsBlockTagLibrary
    override val itemTags = ToStarsItemTagLibrary

    override val particles = ToStarsParticleLibrary
    override val sounds = ToStarsSoundLibrary

    override val eventHandlers = ToStarsEventHandlerLibrary
    override val advancementTriggers = ToStarsAdvancementTriggerLibrary
    override val tickets = ToStarsTicketLibrary
    override val savedData = ToStarsSavedDataLibrary

    override val configuredFeatures = ToStarsConfiguredFeatureLibrary
    override val placedFeatures = ToStarsPlacedFeatureLibrary
    override val biomeMods = ToStarsBiomeModLibrary
}