package net.jidb.to.base.fabric

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import org.slf4j.LoggerFactory
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.block.HorizontalBlock
import net.minecraft.world.level.block.Blocks

object ToBaseFabricMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("to_base")

	override fun onInitialize() {
		ToBaseMod.init()
	}
}