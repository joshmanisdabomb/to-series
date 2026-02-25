package net.jidb.to.stars.fabric

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory
import net.jidb.to.stars.ToSkyAndStarsMod

object ToSkyAndStarsFabricMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("to_sky_and_stars")

	override fun onInitialize() {
		ToSkyAndStarsMod.init()

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
	}
}