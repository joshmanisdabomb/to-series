package net.jidb.to.base.fabric

import net.fabricmc.api.ModInitializer
import net.jidb.to.base.ToBaseMod
import org.slf4j.LoggerFactory

object ToBaseFabricMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("to_base")

	override fun onInitialize() {
		ToBaseMod.init()

		ToBaseMod.blocks.properties.build()
	}
}