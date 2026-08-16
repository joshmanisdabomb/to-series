package net.jidb.to.stars.content

import net.jidb.to.base.api.level.biome.BiomeMod
import net.jidb.to.base.pub.library.BiomeModLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.world.level.levelgen.GenerationStep

object ToStarsBiomeModLibrary : BiomeModLibrary(ToStarsMod.modid) {

    val uranium_ores by this { BiomeMod()
        .addOverworldFeature(
            setOf(ToStarsMod.placedFeatures.uranium_ore, ToStarsMod.placedFeatures.deepslate_uranium_ore),
            GenerationStep.Decoration.UNDERGROUND_ORES
        )
    }

}
