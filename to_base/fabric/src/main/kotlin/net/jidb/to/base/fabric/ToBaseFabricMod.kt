package net.jidb.to.base.fabric

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory
import net.jidb.to.base.ToBaseMod

object ToBaseFabricMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("to_base")

	override fun onInitialize() {
		ToBaseMod.init()

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
	}
}