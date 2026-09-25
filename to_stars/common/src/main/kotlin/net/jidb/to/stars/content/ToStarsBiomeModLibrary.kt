package net.jidb.to.stars.content

import net.jidb.to.base.api.level.biome.BiomeMod
import net.jidb.to.base.pub.library.BiomeModLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.world.level.levelgen.GenerationStep

/**
 * [BiomeModLibrary] implementation holding what this mod adds to the biomes that already exist.
 */
object ToStarsBiomeModLibrary : BiomeModLibrary(ToStarsMod.modid) {

    /**
     * Adds both kinds of uranium ore to the ore generation of every overworld biome.
     */
    val uranium_ores by this { BiomeMod()
        .addOverworldFeature(
            setOf(ToStarsMod.placedFeatures.uranium_ore, ToStarsMod.placedFeatures.deepslate_uranium_ore),
            GenerationStep.Decoration.UNDERGROUND_ORES
        )
    }

}
